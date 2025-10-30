package com.example.workerapp.presentation.screens.change_password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workerapp.data.UserRepository
import com.example.workerapp.data.source.remote.dto.request.ChangePasswordRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _changePasswordUiState =
        MutableStateFlow<ChangePasswordUIState>(ChangePasswordUIState.Idle)
    val changePasswordUiState: MutableStateFlow<ChangePasswordUIState> = _changePasswordUiState

    fun changePassword(newPassword: String, confirmPassword: String) {
        viewModelScope.launch {
            _changePasswordUiState.value = ChangePasswordUIState.Loading

            val request = ChangePasswordRequest(newPassword, confirmPassword)
            val response = userRepository.changePassword(request)

            response.onSuccess {
                _changePasswordUiState.value =
                    ChangePasswordUIState.Success("Mật khẩu được thay đổi thành công")
            }.onFailure {
                _changePasswordUiState.value =
                    ChangePasswordUIState.Error(it.message ?: "Change password failed")
            }
        }
    }
}

sealed class ChangePasswordUIState {
    object Idle : ChangePasswordUIState()
    object Loading : ChangePasswordUIState()
    data class Success(val message: String) : ChangePasswordUIState()
    data class Error(val error: String) : ChangePasswordUIState()
}