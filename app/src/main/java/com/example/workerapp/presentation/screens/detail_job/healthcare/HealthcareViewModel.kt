package com.example.workerapp.presentation.screens.detail_job.healthcare

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workerapp.data.JobServiceRepository
import com.example.workerapp.data.source.model.healthcare.HealthcareJobModel
import com.example.workerapp.data.source.model.healthcare.HealthcareServiceModel
import com.example.workerapp.data.source.remote.JobRemoteImpl
import com.example.workerapp.data.source.remote.dto.NetworkResult
import com.example.workerapp.data.source.remote.dto.request.ApplicationRequest
import com.example.workerapp.data.source.remote.dto.wrapper.HealthServiceWrapper
import com.example.workerapp.utils.ServiceType
import com.example.workerapp.utils.cached.UserSession
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class HealthcareViewModel @Inject constructor(
    private val jobServiceRepository: JobServiceRepository,
    private val healthcareRemoteImpl: JobRemoteImpl
) : ViewModel() {

    private val _uiState = MutableStateFlow(HealthcareUiState())
    val uiState: StateFlow<HealthcareUiState> = _uiState

    private val _applyState = MutableStateFlow<Boolean?>(null)
    val applyState: StateFlow<Boolean?> = _applyState

    fun updateApplyState(value: Boolean?) {
        _applyState.value = value
    }

    fun fetchJobDetail(uid: String) {
        if (uid.isEmpty()) {
            _uiState.value = _uiState.value.copy(error = "Invalid job ID")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            try {
                val result = healthcareRemoteImpl.getHealthcareDetail(uid)
                when (result) {
                    is NetworkResult.Success -> {
                        val job = result.data
                        val serviceData = fetchHealthcareServices(job.services)
                        _uiState.value = HealthcareUiState(
                            isLoading = false,
                            job = job,
                            serviceData = serviceData
                        )
                    }

                    is NetworkResult.Error -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = result.message
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

    private suspend fun fetchHealthcareServices(
        serviceWrappers: List<HealthServiceWrapper>
    ): List<Pair<HealthcareServiceModel, Int>> {
        val healthcareServices = mutableListOf<Pair<HealthcareServiceModel, Int>>()
        for (index in serviceWrappers) {
            val res = jobServiceRepository.getHealthcareServiceByUid(index.uid)
            res.onSuccess { service ->
                healthcareServices.add(service to index.quantity)
            }.onFailure {
                throw Exception("Cannot fetch healthcare service: ${it.localizedMessage}")
            }
        }
        return healthcareServices
    }

    fun applyToJob(uid: String) {
        if (uid.isEmpty()) {
            _uiState.value = _uiState.value.copy(error = "Invalid job ID")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            try {
                val request = ApplicationRequest(
                    workerID = UserSession.uid,
                    jobID = uid,
                    serviceType = ServiceType.HealthcareType
                )

                val result = healthcareRemoteImpl.applyForJob(request)
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

data class HealthcareUiState(
    val isLoading: Boolean = false,
    val job: HealthcareJobModel? = null,
    val serviceData: List<Pair<HealthcareServiceModel, Int>> = emptyList(),
    val error: String? = null
)
