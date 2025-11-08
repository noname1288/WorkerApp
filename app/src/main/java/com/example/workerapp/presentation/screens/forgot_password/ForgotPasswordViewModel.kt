package com.example.workerapp.presentation.screens.forgot_password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workerapp.data.UserRepository
import com.example.workerapp.data.source.remote.dto.request.ForgotPasswordRequest
import com.example.workerapp.data.source.remote.dto.request.ResetPasswordRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ForgotPasswordUiState())
    val uiState = _uiState.asStateFlow()

    private val _resetPasswordUiState = MutableStateFlow(ResetPasswordUiState())
    val resetPasswordUiState = _resetPasswordUiState.asStateFlow()


    private val _email = MutableStateFlow("")
    private val _code = MutableStateFlow("")
    private val _newPassword = MutableStateFlow("")

    fun sendForgotPasswordEmail(email: String) {
        viewModelScope.launch {
            onEmailChange(email)

            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val request = ForgotPasswordRequest(email)

                val response = userRepository.sendEmail(request)
                response.onSuccess {
                    _uiState.value = _uiState.value.copy(isLoading = false, success = true, codeFromEmail = it)

                    onCodeChange(it)
                }.onFailure {
                    _uiState.value = _uiState.value.copy(isLoading = false, error = it.message)
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, error = e.message ?: "An unexpected error occurred.")
            }
        }
    }

    fun submitNewPassword(newPassword: String) {
        viewModelScope.launch {
            _resetPasswordUiState.value = _resetPasswordUiState.value.copy(isLoading = true, error = null)

            try {
                val request = ResetPasswordRequest(
                    email = _email.value,
                    code = _code.value,
                    codeEnter = _code.value,
                    newPassword = newPassword,
                    confirmPassword = newPassword
                )

                val response = userRepository.resetPassword(request)
                response.onSuccess {
                    _resetPasswordUiState.value = _resetPasswordUiState.value.copy(isLoading = false, success = true)

                    onCodeChange(it)
                }.onFailure {
                    _resetPasswordUiState.value = _resetPasswordUiState.value.copy(isLoading = false, error = it.message)
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, error = e.message ?: "An unexpected error occurred.")
            }
        }
    }

    fun onEmailChange(newEmail: String) {
        _email.value = newEmail
    }

    fun onCodeChange(newCode: String) {
        _code.value = newCode
    }

    fun onNewPasswordChange(newPassword: String) {
        _newPassword.value = newPassword
    }
}

data class ForgotPasswordUiState(
    val isLoading: Boolean = false,
    val error : String? = null,
    val success: Boolean = false,
    val codeFromEmail: String? = null
)

data class ResetPasswordUiState(
    val isLoading: Boolean = false,
    val error : String? = null,
    val success: Boolean = false,
)
