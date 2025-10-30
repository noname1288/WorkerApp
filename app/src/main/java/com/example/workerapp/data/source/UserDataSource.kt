package com.example.workerapp.data.source

import com.example.workerapp.data.source.local.room.entity.UserLocalEntity
import com.example.workerapp.data.source.model.base.UserModel
import com.example.workerapp.data.source.remote.dto.NetworkResult
import com.example.workerapp.data.source.remote.dto.request.ChangePasswordRequest
import com.example.workerapp.data.source.remote.dto.request.ForgotPasswordRequest
import com.example.workerapp.data.source.remote.dto.request.UserLoginRequest
import com.example.workerapp.data.source.remote.dto.request.UserLoginWithGGRequest
import com.example.workerapp.data.source.remote.dto.request.UserRegisterRequest
import com.example.workerapp.data.source.remote.dto.request.UserUpdateRequest
import com.example.workerapp.data.source.remote.dto.response.UploadingImageResponse
import com.example.workerapp.data.source.remote.dto.response.UserWrapperResponse
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import retrofit2.Response

interface UserDataSource {
    /* *
    * Local
    * */
    interface Local {
        fun getUserProfile(): Flow<UserLocalEntity?>

        suspend fun saveUserProfile(user: UserLocalEntity)

        suspend fun updateAvatar(userUid: String, avatarUrl: String)

        suspend fun clearUserProfile()
    }

    /* *
    * Remote
    * */
    interface Remote {
        suspend fun login(request: UserLoginRequest): NetworkResult<UserWrapperResponse>

        suspend fun updateFcmToken(fcmToken: String): NetworkResult<Unit>

        suspend fun register(request: UserRegisterRequest): NetworkResult<UserWrapperResponse>

        suspend fun loginWithGoogle(request: UserLoginWithGGRequest): NetworkResult<UserWrapperResponse>

        suspend fun changePassword(request: ChangePasswordRequest) : NetworkResult<Unit>

        suspend fun sendEmail(request: ForgotPasswordRequest) : NetworkResult<Unit>

        suspend fun uploadImage(imagePart: MultipartBody.Part) : NetworkResult<String>

        suspend fun updateProfile(request: UserUpdateRequest) : NetworkResult<UserModel>
    }
}
