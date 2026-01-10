package com.example.workerapp.presentation.screens.detail_job.cleaning

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workerapp.data.JobRepository
import com.example.workerapp.data.JobServiceRepository
import com.example.workerapp.data.source.local.room.entity.ApplicationModel
import com.example.workerapp.data.source.model.cleaning.CleaningJobModel1
import com.example.workerapp.data.source.model.cleaning.CleaningServiceModel
import com.example.workerapp.data.source.remote.JobRemoteImpl
import com.example.workerapp.data.source.remote.dto.NetworkResult
import com.example.workerapp.data.source.remote.dto.request.ApplicationRequest
import com.example.workerapp.data.source.remote.dto.request.CancelApplicationRequest
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
class CleaningViewModel @Inject constructor(
    private val jobServiceRepository: JobServiceRepository,
    private val jobRepository: JobRepository,
    private val cleaningRemoteImpl: JobRemoteImpl
) : ViewModel() {

    private val _uiState = MutableStateFlow(CleaningUiState())
    val uiState: StateFlow<CleaningUiState> = _uiState
    private val _applyJobState = MutableStateFlow<ApplyCleaningJobState>(ApplyCleaningJobState.Idle)
    val appJobState = _applyJobState.asStateFlow()
    private val _appliedState = MutableStateFlow<Boolean?>(null)
    val appliedState = _appliedState.asStateFlow()
    private val _cancelJobState =
        MutableStateFlow<CancelCleaningJobState>(CancelCleaningJobState.Idle)
    val cancelJobState = _cancelJobState.asStateFlow()
    private val _applicationEntity = MutableStateFlow<ApplicationModel?>(null)
    private val TAG = "CleaningViewModel"

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

    fun applyToJob(jobUid: String) {
        Log.d(TAG, "applyToJob: $jobUid")
        if (jobUid.isEmpty()) {
            _applyJobState.value = ApplyCleaningJobState.Error("Invalid User Uid")
            return
        }

        viewModelScope.launch {
            _applyJobState.value = ApplyCleaningJobState.Loading

            val applyResult = withContext(Dispatchers.IO){
                jobRepository.applyJob(
                    ApplicationRequest(
                        workerID = UserSession.uid,
                        jobID = jobUid,
                        serviceType = ServiceType.CleaningType
                    )
                )
            }
            applyResult.onSuccess {
                _applyJobState.value = ApplyCleaningJobState.Success
            }.onFailure { error ->
                _applyJobState.value = ApplyCleaningJobState.Error(error.message ?: "Unknown Error")
            }
        }
    }

    fun cancelApplication(jobUid: String) {
        viewModelScope.launch {
            _cancelJobState.value = CancelCleaningJobState.Loading

            //get lastest applicationID
            val resultGetLastestApplicationById = withContext(Dispatchers.IO) {
                jobRepository.getApplicationByJobId(jobUid)
            }

            resultGetLastestApplicationById.onSuccess { application ->
                Log.d(TAG, "checkIfApplied: $application")
                /* User has not applied this job*/
                if (application == null) {
                    _appliedState.value = false
                    _applicationEntity.value = null
                    return@launch
                }

                /* User has applied this job*/
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
                return@launch
            }

            val applicationEntity = _applicationEntity.value

            val applicationUid = if (applicationEntity == null) {
                _cancelJobState.value =
                    CancelCleaningJobState.Error("Bạn chưa ứng tuyển công việc này")
                return@launch
            } else applicationEntity.applicationId

            Log.d(TAG, "Cancel Order: applicationUid: $applicationUid")
            val cancelResult = withContext(Dispatchers.IO){
                jobRepository.cancelJob(
                    CancelApplicationRequest(
                        applicationUid,
                        ApplicationStatusType.CANCEL
                    )
                )
            }

            cancelResult.onSuccess { applicationWrapper ->
                _cancelJobState.value = CancelCleaningJobState.Success
            }.onFailure { error ->
                _cancelJobState.value =
                    CancelCleaningJobState.Error(error.message ?: "Unknown Error")
            }
        }
    }

    fun checkIfApplied(jobUid: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                jobRepository.getApplicationByJobId(jobUid)
            }.onSuccess { application ->
                Log.d(TAG, "checkIfApplied: $application")
                /* User has not applied this job*/
                if (application == null) {
                    _appliedState.value = false
                    _applicationEntity.value = null
                    return@launch
                }

                /* User has applied this job*/
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
    }

    fun setNewAppliedState(state: Boolean) {
        _appliedState.value = state
    }

    fun resetApplyState(){
        _applyJobState.value = ApplyCleaningJobState.Idle
    }

    fun resetCancelState(){
        _cancelJobState.value = CancelCleaningJobState.Idle
    }
}

data class CleaningUiState(
    val isLoading: Boolean = false,
    val job: CleaningJobModel1? = null,
    val services: List<CleaningServiceModel> = emptyList(),
    val error: String? = null
)

sealed class ApplyCleaningJobState {
    object Idle : ApplyCleaningJobState()
    object Loading : ApplyCleaningJobState()
    object Success : ApplyCleaningJobState()
    data class Error(val message: String) : ApplyCleaningJobState()
}

sealed class CancelCleaningJobState {
    object Idle : CancelCleaningJobState()
    object Loading : CancelCleaningJobState()
    object Success : CancelCleaningJobState()
    data class Error(val message: String) : CancelCleaningJobState()
}


