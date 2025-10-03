package com.example.workerapp.ui.detail.cleaning

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workerapp.data.JobServiceRepository
import com.example.workerapp.data.UserRepository
import com.example.workerapp.data.source.remote.dto.NetworkResult
import com.example.workerapp.data.source.remote.dto.request.ApplicationRequest
import com.example.workerapp.data.source.model.cleaning.CleaningJobModel1
import com.example.workerapp.data.source.model.cleaning.CleaningServiceModel
import com.example.workerapp.data.source.remote.JobRemoteImpl
import com.example.workerapp.utils.ServiceType
import com.example.workerapp.utils.cached.UserSession
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CleaningViewModel @Inject constructor(
    private val jobServiceRepository: JobServiceRepository,
    private val cleaningRemoteImpl: JobRemoteImpl
) : ViewModel() {

    private val _uiState = MutableStateFlow<CleaningUiState>(CleaningUiState.Idle)
    val uiState: StateFlow<CleaningUiState> = _uiState

    private val _applyState = MutableStateFlow<Boolean?>(null)
    val applyState: StateFlow<Boolean?> = _applyState

    fun fetchJobDetail(uid: String) {
        if (uid.isEmpty()) {
            _uiState.value = CleaningUiState.Error("Invalid job ID")
            return
        }
        viewModelScope.launch {
            _uiState.value = CleaningUiState.Loading

            var services = emptyList<CleaningServiceModel>()
            val servicesResult = jobServiceRepository.getCleaningServices()
            servicesResult.onSuccess {
                services = it
                Log.d("CleaningViewModel", "Fetched services: $it")
            }.onFailure {
                Log.e("CleaningViewModel", "Failed to fetch services: ${it.message}")
            }

            val resultJob = cleaningRemoteImpl.getCleaningDetail(uid)
            when (resultJob) {
                is NetworkResult.Success -> {
                    _uiState.value = CleaningUiState.Success(resultJob.data, services)
                }

                is NetworkResult.Error -> {
                    _uiState.value = CleaningUiState.Error(resultJob.message)
                }
            }
        }
    }

    fun updateApplyState(value: Boolean?) {
        _applyState.value = value
    }

    fun applyToJob(uid: String) {
        if (uid.isEmpty()) {
            _uiState.value = CleaningUiState.Error("Invalid job ID")
            return
        }
        viewModelScope.launch {
            _uiState.value = CleaningUiState.Loading
            try {
                val request = ApplicationRequest(
                    workerID = UserSession.uid,
                    jobID = uid,
                    serviceType = ServiceType.CleaningType
                )

                Log.d("CleaningViewModel", "Applying with request: $request")

                val result = cleaningRemoteImpl.applyForJob(request)
                when (result) {
                    is NetworkResult.Success -> {
                        _applyState.value = true
                    }

                    is NetworkResult.Error -> {
                        _applyState.value = false
                        _uiState.value = CleaningUiState.Error(result.message)
                    }
                }
            } catch (e: Exception) {
                _applyState.value = false
                _uiState.value = CleaningUiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}

sealed class CleaningUiState {
    object Loading : CleaningUiState()
    object Idle : CleaningUiState()
    data class Success(val job: CleaningJobModel1, val services: List<CleaningServiceModel>) :
        CleaningUiState()

    data class Error(val message: String) : CleaningUiState()
}

