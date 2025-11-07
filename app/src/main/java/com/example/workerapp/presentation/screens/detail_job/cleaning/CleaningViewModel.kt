package com.example.workerapp.ui.detail.cleaning

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workerapp.data.JobServiceRepository
import com.example.workerapp.data.source.model.cleaning.CleaningJobModel1
import com.example.workerapp.data.source.model.cleaning.CleaningServiceModel
import com.example.workerapp.data.source.remote.JobRemoteImpl
import com.example.workerapp.data.source.remote.dto.NetworkResult
import com.example.workerapp.data.source.remote.dto.request.ApplicationRequest
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

    private val _uiState = MutableStateFlow(CleaningUiState())
    val uiState: StateFlow<CleaningUiState> = _uiState

    private val _applyState = MutableStateFlow<Boolean?>(null)
    val applyState: StateFlow<Boolean?> = _applyState

    fun fetchJobDetail(uid: String) {
        if (uid.isEmpty()) {
            _uiState.value = _uiState.value.copy(error = "Invalid job ID")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            try {
                // Fetch cleaning services
                val servicesResult = jobServiceRepository.getCleaningServices()
                val services = servicesResult.getOrNull() ?: emptyList()

                // Fetch job detail
                when (val resultJob = cleaningRemoteImpl.getCleaningDetail(uid)) {
                    is NetworkResult.Success -> {
                        _uiState.value = CleaningUiState(
                            job = resultJob.data,
                            services = services,
                            isLoading = false
                        )
                    }

                    is NetworkResult.Error -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = resultJob.message
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.localizedMessage ?: "Unexpected error"
                )
            }
        }
    }

    fun updateApplyState(value: Boolean?) {
        _applyState.value = value
    }

    fun applyToJob(uid: String) {
        if (uid.isEmpty()) {
            _uiState.value = _uiState.value.copy(error = "Invalid job ID")
            return
        }

        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true, error = null)

                val request = ApplicationRequest(
                    workerID = UserSession.uid,
                    jobID = uid,
                    serviceType = ServiceType.CleaningType
                )

                val result = cleaningRemoteImpl.applyForJob(request)
                if (result is NetworkResult.Success) {
                    _applyState.value = true
                } else if (result is NetworkResult.Error) {
                    _applyState.value = false
                    _uiState.value = _uiState.value.copy(error = result.message)
                }
            } catch (e: Exception) {
                _applyState.value = false
                _uiState.value = _uiState.value.copy(error = e.localizedMessage ?: "Unknown error")
            } finally {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }
}

data class CleaningUiState(
    val isLoading: Boolean = false,
    val job: CleaningJobModel1? = null,
    val services: List<CleaningServiceModel> = emptyList(),
    val error: String? = null
)
