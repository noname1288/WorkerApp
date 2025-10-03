package com.example.workerapp.presentation.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workerapp.data.TokenRepository
import com.example.workerapp.data.source.remote.JobRemoteImpl
import com.example.workerapp.data.source.remote.dto.ApplicationWrapper
import com.example.workerapp.data.source.remote.dto.NetworkResult
import com.example.workerapp.utils.cached.UserSession
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val tokenRepository: TokenRepository,
    private val jobRemoteImpl: JobRemoteImpl
) : ViewModel() {

    private val _applicationsState = MutableStateFlow<ApplicationsUiState>(ApplicationsUiState.Idle)
    val applicationsState: MutableStateFlow<ApplicationsUiState> = _applicationsState

    fun fetchApplications() {
        val userUid = UserSession.uid

        if (userUid.isNullOrEmpty()) {
            _applicationsState.value = ApplicationsUiState.Error("User not logged in")
            return
        }

        viewModelScope.launch {
            _applicationsState.value = ApplicationsUiState.Loading

            try {
                val result = jobRemoteImpl.getApplication(userUid)

                when (result) {
                    is NetworkResult.Error -> {
                        _applicationsState.value = ApplicationsUiState.Error(result.message)
                    }

                    is NetworkResult.Success -> {
                        _applicationsState.value = ApplicationsUiState.Success(result.data)
                    }
                }
            } catch (e: Exception) {
                _applicationsState.value = ApplicationsUiState.Error(e.message ?: "Unknown error")
            }
        }


    }

}

sealed class ApplicationsUiState {
    object Idle : ApplicationsUiState()
    object Loading : ApplicationsUiState()
    data class Success(val data: List<ApplicationWrapper>) : ApplicationsUiState()
    data class Error(val message: String) : ApplicationsUiState()
}