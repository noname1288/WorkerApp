package com.example.workerapp.presentation.screens.forgot_password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workerapp.data.UserRepository
import com.example.workerapp.data.source.remote.dto.request.ForgotPasswordRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<ForgotPasswordUiState>(ForgotPasswordUiState.Idle)
    val uiState: StateFlow<ForgotPasswordUiState> = _uiState.asStateFlow()

    fun sendForgotPasswordEmail(email: String) {
        viewModelScope.launch {
            _uiState.value = ForgotPasswordUiState.Loading
            try {
                val request = ForgotPasswordRequest(email)

                val response = userRepository.sendEmail(request)
                response.onSuccess {
                    _uiState.value =
                        ForgotPasswordUiState.Success("Password reset email sent successfully.")
                }.onFailure {
                    _uiState.value = ForgotPasswordUiState.Error(it.message ?: "Failed to send password reset email.")
                }
            } catch (e: Exception) {
                _uiState.value = ForgotPasswordUiState.Error("Failed to send password reset email.")
            }
        }
    }
}

sealed class ForgotPasswordUiState {
    object Idle : ForgotPasswordUiState()
    object Loading : ForgotPasswordUiState()
    data class Success(val message: String) : ForgotPasswordUiState()
    data class Error(val error: String) : ForgotPasswordUiState()
}
