package com.example.workerapp.ui.detail.cleaning

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workerapp.data.JobRepository
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
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CleaningViewModel @Inject constructor(
    private val jobServiceRepository: JobServiceRepository,
    private val jobRepository: JobRepository,
    private val cleaningRemoteImpl: JobRemoteImpl
) : ViewModel() {

    private val _uiState = MutableStateFlow(CleaningUiState())
    val uiState: StateFlow<CleaningUiState> = _uiState
    private val _applyJobState = MutableStateFlow<ApplyJobState>(ApplyJobState.Idle)
    val appJobState = _applyJobState.asStateFlow()
    private val _appliedState = MutableStateFlow<Boolean?>(null)
    val appliedState = _appliedState.asStateFlow()
    private val _cancelJobState = MutableStateFlow<CancelJobState>(CancelJobState.Idle)
    val cancelJobState = _cancelJobState.asStateFlow()

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

    fun applyToJob(uid: String) {
        if (uid.isEmpty()) {
            _applyJobState.value = ApplyJobState.Error("Invalid User Uid")
            return
        }

        viewModelScope.launch {
            try {
                _applyJobState.value = ApplyJobState.Loading

                val request = ApplicationRequest(
                    workerID = UserSession.uid,
                    jobID = uid,
                    serviceType = ServiceType.CleaningType
                )

                val result = cleaningRemoteImpl.applyForJob(request)
                if (result is NetworkResult.Success) {
                    _applyJobState.value = ApplyJobState.Success
                } else if (result is NetworkResult.Error) {
                    _applyJobState.value  = ApplyJobState.Error(result.message)
                }
            }catch (e : Exception){
                _applyJobState.value  = ApplyJobState.Error(e.message?: "Unknown Error")
            }
        }
    }

    fun insertApplicationToLocal(){
        viewModelScope.launch {

        }
    }

    fun cancelApplication(jobUid: String){
        viewModelScope.launch {
            _cancelJobState.value = CancelJobState.Loading

            val result = jobRepository.cancelJob(
                serviceType = ServiceType.CleaningType,
                jobUid = jobUid,
            )

            result.onSuccess {
               _cancelJobState.value = CancelJobState.Success
            }.onFailure {
                _cancelJobState.value = CancelJobState.Error(it.message ?: "Unknown Error")
            }
        }
    }

    fun checkIfApplied(jobUid: String){
        viewModelScope.launch {
            val result = jobRepository.checkApplicationByJobUid(jobUid)

            result.onSuccess {
                _appliedState.value = it
            }.onFailure {
                _appliedState.value = null
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


