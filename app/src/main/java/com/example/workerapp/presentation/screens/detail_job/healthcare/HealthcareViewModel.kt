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
import com.example.workerapp.data.source.remote.dto.response.CancelApplicationWrapper
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
    private val _applyJobState = MutableStateFlow<ApplyJobState>(ApplyJobState.Idle)
    val appJobState = _applyJobState.asStateFlow()
    private val _appliedState = MutableStateFlow<Boolean?>(null)
    val appliedState = _appliedState.asStateFlow()
    private val _cancelJobState = MutableStateFlow<CancelJobState>(CancelJobState.Idle)
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

//    fun applyToJob(jobUid: String) {
//        if (jobUid.isEmpty()) {
//            _applyJobState.value = ApplyJobState.Error("Invalid job ID")
//            return
//        }
//
//        viewModelScope.launch {
//            try {
//                _applyJobState.value = ApplyJobState.Loading
//
//                val request = ApplicationRequest(
//                    workerID = UserSession.uid,
//                    jobID = jobUid,
//                    serviceType = ServiceType.HealthcareType
//                )
//
//                val result = healthcareRemoteImpl.applyForJob(request)
//                if (result is NetworkResult.Success) {
//                    //update local
//                    insertApplicationToLocal(jobUid)
//
//                    _applyJobState.value = ApplyJobState.Success
//                    _appliedState.value = true
//                } else if (result is NetworkResult.Error) {
//                    _applyJobState.value = ApplyJobState.Error(result.message)
//                }
//            } catch (e: Exception) {
//                _applyJobState.value = ApplyJobState.Error(e.message ?: "Unknown Error")
//            }
//        }
//    }

//    suspend fun insertApplicationToLocal(jobUid: String) {
//        val currentUser = UserSession.uid
//        if (currentUser == null) {
//            _applyJobState.value = ApplyJobState.Error("Error to find current user")
//            return
//        }
//
//        val applicationResponse = healthcareRemoteImpl.getApplication(currentUser)
//
//        when (applicationResponse) {
//            is NetworkResult.Error -> {
//                _applyJobState.value =
//                    ApplyJobState.Error("can't insert new application into local")
//            }
//
//            is NetworkResult.Success -> {
//                val applicationList = applicationResponse.data
//
//                if (applicationList.isNotEmpty()) {
//                    val newApplicationDto = applicationList[0]
//
//                    if (newApplicationDto.job.uid == jobUid)
//                        jobRepository.insertApplicationToLocal(newApplicationDto)
//                    else
//                        ApplyJobState.Error("new application (${newApplicationDto.job.uid}) doesn't match with current job($jobUid)")
//                }
//            }
//        }
//    }

    fun cancelApplication() {
        viewModelScope.launch {
            _cancelJobState.value = CancelJobState.Loading

            val applicationEntity = _applicationEntity.value

            val applicationUid = if (applicationEntity == null) {
                _cancelJobState.value = CancelJobState.Error("Bạn chưa ứng tuyển công việc này")
                return@launch
            } else applicationEntity.applicationId

            val request = CancelApplicationRequest(
                applicationUid,
                ApplicationStatusType.CANCEL
            )

            val result = jobRepository.cancelJob(request)

            result.onSuccess { applicationWrapper ->
                //update local
                val result = withContext(Dispatchers.IO) {
                    updateStatusByApplicationId(applicationWrapper)
                }

                if (result == true) {
                    _cancelJobState.value = CancelJobState.Success
                    _appliedState.value = false
                } else {
                    _appliedState.value = true
                    _cancelJobState.value = CancelJobState.Error("Fail to update local")
                }
            }.onFailure {
                _cancelJobState.value = CancelJobState.Error(it.message ?: "Unknown Error")
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

    suspend fun updateStatusByApplicationId(applicationWrapper: CancelApplicationWrapper): Boolean? {
        val applicationId = applicationWrapper.uid
        val newStatus = applicationWrapper.status

        val result = jobRepository.updateStatusByApplicationId(applicationId, newStatus)
        result.onSuccess {
            return true
        }

        return null
    }
}

data class HealthcareUiState(
    val isLoading: Boolean = false,
    val job: HealthcareJobModel? = null,
    val serviceData: List<Pair<HealthcareServiceModel, Int>> = emptyList(),
    val error: String? = null
)

sealed class ApplyJobState {
    object Idle : ApplyJobState()
    object Loading : ApplyJobState()
    object Success : ApplyJobState()
    data class Error(val message: String) : ApplyJobState()
}

sealed class CancelJobState {
    object Idle : CancelJobState()
    object Loading : CancelJobState()
    object Success : CancelJobState()
    data class Error(val message: String) : CancelJobState()
}
