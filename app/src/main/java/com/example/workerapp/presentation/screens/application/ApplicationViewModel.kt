package com.example.workerapp.presentation.screens.application

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workerapp.data.source.remote.JobRemoteImpl
import com.example.workerapp.data.source.remote.dto.NetworkResult
import com.example.workerapp.data.source.remote.dto.response.ApplicationDto
import com.example.workerapp.utils.cached.UserSession
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ApplicationViewModel @Inject constructor(
    private val jobRemoteImpl: JobRemoteImpl
) : ViewModel() {

    private val _applicationsState = MutableStateFlow<com.example.workerapp.presentation.screens.profile.ApplicationsUiState>(
        com.example.workerapp.presentation.screens.profile.ApplicationsUiState.Idle)
    val applicationsState: MutableStateFlow<com.example.workerapp.presentation.screens.profile.ApplicationsUiState> = _applicationsState

    fun fetchApplications() {
        val userUid = UserSession.uid

        if (userUid.isNullOrEmpty()) {
            _applicationsState.value = com.example.workerapp.presentation.screens.profile.ApplicationsUiState.Error("User not logged in")
            return
        }

        viewModelScope.launch {
            _applicationsState.value = com.example.workerapp.presentation.screens.profile.ApplicationsUiState.Loading

            try {
                val result = jobRemoteImpl.getApplication(userUid)

                when (result) {
                    is NetworkResult.Error -> {
                        _applicationsState.value = com.example.workerapp.presentation.screens.profile.ApplicationsUiState.Error(result.message)
                    }

                    is NetworkResult.Success -> {
                        _applicationsState.value = com.example.workerapp.presentation.screens.profile.ApplicationsUiState.Success(result.data)
                    }
                }
            } catch (e: Exception) {
                _applicationsState.value = com.example.workerapp.presentation.screens.profile.ApplicationsUiState.Error(e.message ?: "Unknown error")
            }
        }


    }

}

sealed class ApplicationsUiState {
    object Idle : ApplicationsUiState()
    object Loading : ApplicationsUiState()
    data class Success(val data: List<ApplicationDto>) : ApplicationsUiState()
    data class Error(val message: String) : ApplicationsUiState()
}