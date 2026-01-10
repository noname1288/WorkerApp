package com.example.workerapp.presentation.screens.application

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workerapp.data.JobRepository
import com.example.workerapp.data.source.remote.JobRemoteImpl
import com.example.workerapp.data.source.remote.dto.response.ApplicationDto
import com.example.workerapp.utils.cached.UserSession
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ApplicationViewModel @Inject constructor(
    private val jobRemoteImpl: JobRemoteImpl,
    private val jobRepository: JobRepository
) : ViewModel() {

    private val _applicationsState = MutableStateFlow<ApplicationsUiState>(
        ApplicationsUiState.Idle
    )
    val applicationsState: MutableStateFlow<ApplicationsUiState> = _applicationsState

    fun fetchApplications() {
        viewModelScope.launch {
            _applicationsState.value = ApplicationsUiState.Loading

            var currentUser = ""
            UserSession.requireUserId().onSuccess {
                currentUser = it
            }.onFailure { error ->
                _applicationsState.value =
                    ApplicationsUiState.Error(error.message ?: "Unknown Error")
                return@launch
            }

            if (currentUser.isEmpty())
                return@launch

            jobRemoteImpl.getApplication(currentUser).onSuccess { data ->
                _applicationsState.value = ApplicationsUiState.Success(data)

                syncApplicationsFromRemote()
            }.onFailure { error ->
                _applicationsState.value =
                    ApplicationsUiState.Error(error.message ?: "Unknown Error")
            }
        }
    }

    fun syncApplicationsFromRemote() {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                jobRepository.syncApplicationsFromRemote()
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