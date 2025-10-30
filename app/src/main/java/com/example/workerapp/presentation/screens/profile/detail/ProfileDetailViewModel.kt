package com.example.workerapp.presentation.screens.profile.detail

import android.app.Application
import android.net.Uri
import android.webkit.MimeTypeMap
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workerapp.data.UserRepository
import com.example.workerapp.data.source.local.room.entity.UserLocalEntity
import com.example.workerapp.data.source.remote.dto.request.UserUpdateRequest
import com.example.workerapp.utils.cached.UserSession
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@HiltViewModel
class ProfileDetailViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val app: Application,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _profileDetailState =
        MutableStateFlow<ProfileDetailUiState>(ProfileDetailUiState.Idle)
    val profileDetailState: StateFlow<ProfileDetailUiState> = _profileDetailState

    val userFlow: StateFlow<UserLocalEntity?> = userRepository.getUserProfile().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        null
    )

    private val _form = MutableStateFlow(
        savedStateHandle.get<ProfileFormState>("profileFormState") ?: ProfileFormState()
    )
    val form : StateFlow<ProfileFormState> = _form

    private fun persist(){
        savedStateHandle["profileFormState"] = _form.value
    }

    fun onUsernameChange(v: String) { _form.update { it.copy(username = v) }.also { persist() } }
    fun onGenderChange(v: String)   { _form.update { it.copy(gender = v) }.also { persist() } }
    fun onTelChange(v: String)      { _form.update { it.copy(tel = v) }.also { persist() } }
    fun onDobChange(v: String)      { _form.update { it.copy(dob = v) }.also { persist() } }
    fun onLocationChange(v: String) { _form.update { it.copy(location = v) }.also { persist() } }
    fun onImagePicked(uri: String?)    { _form.update { it.copy(imageUri = uri) }.also { persist() } }
    fun onLocationPicked(address: String, lat: Double, lng: Double) =
        _form.update { it.copy(location = address, lat = lat, lng = lng) }.also { persist() }

    fun updateProfile(
        username: String,
        gender: String,
        tel: String,
        dob: String,
        location: String,
        imageUri: Uri?
    ) {
        viewModelScope.launch {
            _profileDetailState.value = ProfileDetailUiState.Loading
            try {
                val currentUser = userFlow.firstOrNull()
                if (currentUser == null) {
                    _profileDetailState.value =
                        ProfileDetailUiState.Error("User not found. Please login again.")
                    return@launch
                } else {
                    // Upload avatar nếu có ảnh mới
                    val avatarUrl = if (imageUri != null) {
                        val result = uploadAvatar(imageUri)
                        if (result.isFailure) {
                            _profileDetailState.value = ProfileDetailUiState.Error(
                                result.exceptionOrNull()?.message ?: "Upload avatar failed"
                            )
                            return@launch
                        }
                        result.getOrNull()
                    } else currentUser.avatar

                    val request = UserUpdateRequest(
                        uid = currentUser.uid,
                        username = username,
                        gender = gender,
                        dob = dob,
                        tel = tel,
                        location = location,
                        avatar = avatarUrl!!,
                        email = currentUser.email,
                        provider = currentUser.provider,
                        role = currentUser.role
                    )

                    userRepository.updateProfile(request)
                }
            } catch (e: Exception) {
                _profileDetailState.value =
                    ProfileDetailUiState.Error(e.message ?: "Update profile failed")
            }
        }
    }

    suspend fun uploadAvatar(uri: Uri): Result<String> {
        val currentUser = userFlow.value

        if (currentUser != null) {
            val imagePath = withContext(Dispatchers.IO){
                uriToMultipart(uri)
            }
            return userRepository.uploadImage(currentUser.uid, imagePath)
        } else {
            return Result.failure(Exception("User not found"))
        }
    }

    /** Convert URI -> MultipartBody.Part */
    private fun uriToMultipart(uri: Uri): MultipartBody.Part {
        val context = app.applicationContext
        val contentResolver = context.contentResolver

        val mimeType = contentResolver.getType(uri) ?: "image/*"

        val extension = MimeTypeMap.getSingleton()
            .getExtensionFromMimeType(mimeType) ?: "jpg"

        val file = File(context.cacheDir, "upload_${System.currentTimeMillis()}.$extension")

        val inputStream = contentResolver.openInputStream(uri)
            ?: throw IllegalStateException("Cannot open URI stream")
        file.outputStream().use { output ->
            inputStream.copyTo(output)
        }

        val requestFile = file.asRequestBody(mimeType.toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("image", file.name, requestFile)
    }

    fun updateImage(uri: Uri) {
        viewModelScope.launch {
            _profileDetailState.value = ProfileDetailUiState.Loading

            try {
                UserSession.uid?.let {
                    val response = userRepository.uploadImage(it, uriToMultipart(uri))

                    response.onFailure {
                        _profileDetailState.value =
                            ProfileDetailUiState.Error(it.message ?: "Upload image failed")
                    }.onSuccess {
                        _profileDetailState.value =
                            ProfileDetailUiState.Success("Đồng bộ ảnh đại diện thành công")
                    }
                }
            } catch (e: Exception) {
                _profileDetailState.value =
                    ProfileDetailUiState.Error(e.message ?: "Upload image failed")
            }
        }
    }
}

@Parcelize
data class ProfileFormState(
    val username: String = "",
    val gender: String = "",
    val tel: String = "",
    val dob: String = "",
    val location: String = "",     // địa chỉ hiển thị
    val lat: Double? = null,       // toạ độ chọn từ map
    val lng: Double? = null,
    val imageUri: String? = null
) : Parcelable

sealed class ProfileDetailUiState {
    object Idle : ProfileDetailUiState()
    object Loading : ProfileDetailUiState()
    data class Success(val message: String) : ProfileDetailUiState()
    data class Error(val message: String) : ProfileDetailUiState()
}
