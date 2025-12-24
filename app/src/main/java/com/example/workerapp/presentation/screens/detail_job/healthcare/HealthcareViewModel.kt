package com.example.workerapp.presentation.screens.detail_job.healthcare

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workerapp.data.JobRepository
import com.example.workerapp.data.JobServiceRepository
import com.example.workerapp.data.source.local.room.entity.ApplicationModel
import com.example.workerapp.data.source.model.healthcare.HealthcareJobModel
import com.example.workerapp.data.source.model.healthcare.HealthcareServiceModel
import com.example.workerapp.data.source.remote.JobRemoteImpl
import com.example.workerapp.data.source.remote.dto.NetworkResult
import com.example.workerapp.data.source.remote.dto.request.ApplicationRequest
import com.example.workerapp.data.source.remote.dto.request.CancelApplicationRequest
import com.example.workerapp.data.source.remote.dto.wrapper.HealthServiceWrapper
import com.example.workerapp.utils.ApplicationStatusType
import com.example.workerapp.utils.ServiceType
import com.example.workerapp.utils.cached.UserSession
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
@HiltViewModel
class HealthcareViewModel @Inject constructor(
    private val jobServiceRepository: JobServiceRepository,
    private val jobRepository: JobRepository,
    private val healthcareRemoteImpl: JobRemoteImpl
) : ViewModel() {

    private val _uiState = MutableStateFlow(HealthcareUiState())
    val uiState: StateFlow<HealthcareUiState> = _uiState
    private val _applyJobState = MutableStateFlow<ApplyHealthcareJobState>(ApplyHealthcareJobState.Idle)
    val appJobState = _applyJobState.asStateFlow()
    private val _appliedState = MutableStateFlow<Boolean?>(null)
    val appliedState = _appliedState.asStateFlow()
    private val _cancelJobState = MutableStateFlow<CancelHealthcareJobState>(CancelHealthcareJobState.Idle)
    val cancelJobState = _cancelJobState.asStateFlow()
    private val _applicationEntity = MutableStateFlow<ApplicationModel?>(null)

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

    fun applyToJob(jobUid: String) {
        if (jobUid.isEmpty()) {
            _applyJobState.value = ApplyHealthcareJobState.Error("Invalid job ID")
            return
        }

        viewModelScope.launch {
            _applyJobState.value = ApplyHealthcareJobState.Loading

            jobRepository.applyJob(
                ApplicationRequest(
                    workerID = UserSession.uid,
                    jobID = jobUid,
                    serviceType = ServiceType.HealthcareType
                )
            ).onSuccess {
                //check event: data is updated (local)
                checkIfApplied(jobUid)

                _applyJobState.value = ApplyHealthcareJobState.Success
            }.onFailure { error ->
                _applyJobState.value = ApplyHealthcareJobState.Error(error.message ?: "Unknown Error")
            }
        }
    }

    fun cancelApplication() {
        viewModelScope.launch {
            _cancelJobState.value = CancelHealthcareJobState.Loading

            val applicationEntity = _applicationEntity.value

            val applicationUid = if (applicationEntity == null) {
                _cancelJobState.value = CancelHealthcareJobState.Error("Bạn chưa ứng tuyển công việc này")
                return@launch
            } else applicationEntity.applicationId

            jobRepository.cancelJob(
                CancelApplicationRequest(
                    applicationUid,
                    ApplicationStatusType.CANCEL
                )
            ).onSuccess { applicationWrapper ->
                _cancelJobState.value = CancelHealthcareJobState.Success
            }.onFailure { error ->
                _cancelJobState.value = CancelHealthcareJobState.Error(error.message ?: "Unknown Error")
                _appliedState.value = false
            }
        }
    }

    suspend fun checkIfApplied(jobUid: String) {
        val result = withContext(Dispatchers.IO) {
            jobRepository.getApplicationByJobId(jobUid)
        }

        Log.d("HealthcareViewModel", "$result")

        result.onSuccess { application ->
            if (application == null) {
                _appliedState.value = false
                _applicationEntity.value = null
                return
            }

            val status = application.status
            if (status == ApplicationStatusType.WAITING) {
                _appliedState.value = true
                _applicationEntity.value = application
            } else {
                _appliedState.value = false
                _applicationEntity.value = null
            }

        }.onFailure {
            _appliedState.value = null
            _applicationEntity.value = null
        }
    }

    fun setNewAppliedState(state: Boolean) {
        _appliedState.value = state
    }
}

data class HealthcareUiState(
    val isLoading: Boolean = false,
    val job: HealthcareJobModel? = null,
    val serviceData: List<Pair<HealthcareServiceModel, Int>> = emptyList(),
    val error: String? = null
)

sealed class ApplyHealthcareJobState {
    object Idle : ApplyHealthcareJobState()
    object Loading : ApplyHealthcareJobState()
    object Success : ApplyHealthcareJobState()
    data class Error(val message: String) : ApplyHealthcareJobState()
}

sealed class CancelHealthcareJobState {
    object Idle : CancelHealthcareJobState()
    object Loading : CancelHealthcareJobState()
    object Success : CancelHealthcareJobState()
    data class Error(val message: String) : CancelHealthcareJobState()
}
