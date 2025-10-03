package com.example.workerapp.presentation.screens.detail.healcare

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workerapp.data.JobServiceRepository
import com.example.workerapp.data.source.remote.dto.NetworkResult
import com.example.workerapp.data.source.remote.dto.request.ApplicationRequest
import com.example.workerapp.data.source.model.healthcare.HealthcareJobModel
import com.example.workerapp.data.source.model.healthcare.HealthcareServiceModel
import com.example.workerapp.data.source.remote.JobRemoteImpl
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
    private val _uiState = MutableStateFlow<HealthcareUiState>(HealthcareUiState.Idle)
    val uiState: MutableStateFlow<HealthcareUiState> = _uiState

    private val _applyState = MutableStateFlow<Boolean?>(null)
    val applyState: StateFlow<Boolean?> = _applyState

    fun updateApplyState(value: Boolean?) {
        _applyState.value = value
    }

    fun fetchJobDetail(uid: String) {
        if (uid.isEmpty()) {
            _uiState.value = HealthcareUiState.Error("Invalid job ID")
            return
        }
        viewModelScope.launch {
            _uiState.value = HealthcareUiState.Loading
            try {
                val result = healthcareRemoteImpl.getHealthcareDetail(uid)

                when (result) {
                    is NetworkResult.Success -> {
                        val job = result.data
                        val serviceWrapper = job.services

                        val fetchedService = fetchHealthcareServices(serviceWrapper)

                        _uiState.value = HealthcareUiState.Success(result.data, fetchedService)
                    }

                    is NetworkResult.Error -> {
                        _uiState.value = HealthcareUiState.Error(result.message)
                    }
                }
            } catch (e: Exception) {
                _uiState.value = HealthcareUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    suspend fun fetchHealthcareServices(serviceWrappers: List<HealthServiceWrapper>): List<Pair<HealthcareServiceModel, Int>> {
        val healthcareServices = mutableListOf<Pair<HealthcareServiceModel, Int>>()

        for (index in serviceWrappers) {
            val res = jobServiceRepository.getHealthcareServiceByUid(index.serviceID)
            res.onSuccess {
                healthcareServices.add(it to index.quantity)
            }.onFailure {
                throw Exception("Can't fetch healthcare service from local Storage in HealthcareViewModel")
            }
        }

        return healthcareServices
    }

    fun applyToJob(uid: String) {
        if (uid.isEmpty()) {
            _uiState.value = HealthcareUiState.Error("Invalid job ID")
            return
        }
        viewModelScope.launch {
            try {
                val request = ApplicationRequest(
                    workerID = UserSession.uid,
                    jobID = uid,
                    serviceType = ServiceType.HealthcareType
                )

                val result = healthcareRemoteImpl.applyForJob(request)
                when (result) {
                    is NetworkResult.Success -> {
                        _applyState.value = true
                    }

                    is NetworkResult.Error -> {
                        _uiState.value = HealthcareUiState.Error(result.message)
                    }
                }
            } catch (e: Exception) {
                _applyState.value = true
                _uiState.value = HealthcareUiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}

sealed class HealthcareUiState {
    object Loading : HealthcareUiState()
    object Idle : HealthcareUiState()
    data class Success(
        val data: HealthcareJobModel,
        val serviceData: List<Pair<HealthcareServiceModel, Int>>
    ) :
        HealthcareUiState()

    data class Error(val message: String) : HealthcareUiState()
}