package com.example.workerapp.presentation.screens.detail_job.maintenance

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workerapp.data.JobRepository
import com.example.workerapp.data.JobServiceRepository
import com.example.workerapp.data.source.local.room.entity.ApplicationModel
import com.example.workerapp.data.source.model.maintenance.MaintenanceJobResponse
import com.example.workerapp.data.source.model.maintenance.MaintenanceServiceModel
import com.example.workerapp.data.source.model.maintenance.PowerWrapper
import com.example.workerapp.data.source.remote.JobRemoteImpl
import com.example.workerapp.data.source.remote.dto.NetworkResult
import com.example.workerapp.data.source.remote.dto.request.ApplicationRequest
import com.example.workerapp.data.source.remote.dto.request.CancelApplicationRequest
import com.example.workerapp.data.source.remote.dto.response.CancelApplicationWrapper
import com.example.workerapp.data.source.remote.dto.wrapper.MaintenanceServiceDto
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
class MaintenanceViewModel @Inject constructor(
    private val jobRemoteImpl: JobRemoteImpl,
    private val jobServiceRepository: JobServiceRepository,
    private val jobRepository: JobRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FetchMaintenanceJobState())
    val uiState: StateFlow<FetchMaintenanceJobState> = _uiState
    private val _applyJobState = MutableStateFlow<ApplyMaintenanceJobState>(ApplyMaintenanceJobState.Idle)
    val appJobState = _applyJobState.asStateFlow()
    private val _appliedState = MutableStateFlow<Boolean?>(null)
    val appliedState = _appliedState.asStateFlow()
    private val _cancelJobState = MutableStateFlow<CancelMaintenanceJobState>(CancelMaintenanceJobState.Idle)
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
                val result = jobRemoteImpl.getMaintenanceDetail(uid)
                when (result) {
                    is NetworkResult.Success -> {
                        val jobData = result.data
                        val serviceData = fetchMaintenanceService(jobData.services)
                        _uiState.value = FetchMaintenanceJobState(
                            job = jobData,
                            services = serviceData,
                            isLoading = false
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

    private suspend fun fetchMaintenanceService(
        serviceDtoList: List<MaintenanceServiceDto>
    ): List<Pair<MaintenanceServiceModel, List<PowerWrapper>>> {
        val mapData = mutableListOf<Pair<MaintenanceServiceModel, List<PowerWrapper>>>()

        for (item in serviceDtoList) {
            val service = jobServiceRepository.getMaintenanceServiceByUid(item.uid)
            val powerList = mutableListOf<PowerWrapper>()

            for (index in item.powers) {
                val powerModelResult = jobServiceRepository.getPowerModelByUid(index.uid)
                powerModelResult.onSuccess {
                    powerList.add(
                        PowerWrapper(
                            uid = it.uid,
                            name = it.name,
                            price = it.price,
                            priceAction = it.priceAction,
                            quantity = index.quantity,
                            quantityAction = index.quantityAction
                        )
                    )
                }.onFailure {
                    throw RuntimeException("Can't fetch power model from local storage")
                }
            }

            mapData.add(Pair(service.getOrThrow(), powerList))
        }

        return mapData
    }

    fun applyToJob(jobUid: String) {
        if (jobUid.isEmpty()) {
            _applyJobState.value = ApplyMaintenanceJobState.Error("Invalid job ID")
            return
        }

        viewModelScope.launch {
            _applyJobState.value = ApplyMaintenanceJobState.Loading

            jobRepository.applyJob(
                ApplicationRequest(
                    workerID = UserSession.uid,
                    jobID = jobUid,
                    serviceType = ServiceType.MaintenanceType
                )
            ).onSuccess {
                //check event: data is updated (local)
                checkIfApplied(jobUid)

                _applyJobState.value = ApplyMaintenanceJobState.Success
            }.onFailure { error ->
                _applyJobState.value = ApplyMaintenanceJobState.Error(error.message ?: "Unknown Error")
            }
        }
    }

    fun cancelApplication() {
        viewModelScope.launch {
            _cancelJobState.value = CancelMaintenanceJobState.Loading

            val applicationEntity = _applicationEntity.value

            val applicationUid = if (applicationEntity == null) {
                _cancelJobState.value = CancelMaintenanceJobState.Error("Bạn chưa ứng tuyển công việc này")
                return@launch
            } else applicationEntity.applicationId

            jobRepository.cancelJob(
                CancelApplicationRequest(
                    applicationUid,
                    ApplicationStatusType.CANCEL
                )
            ).onSuccess { applicationWrapper ->
                _cancelJobState.value = CancelMaintenanceJobState.Success
            }.onFailure { error ->
                _cancelJobState.value = CancelMaintenanceJobState.Error(error.message ?: "Unknown Error")
            }
        }
    }

    fun setNewAppliedState(state: Boolean) {
        _appliedState.value = state
    }


    suspend fun checkIfApplied(jobUid: String) {
        val result = withContext(Dispatchers.IO) {
            jobRepository.getApplicationByJobId(jobUid)
        }

        Log.d("MaintenanceViewModel", "$result")

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

data class FetchMaintenanceJobState(
    val isLoading: Boolean = false,
    val job: MaintenanceJobResponse? = null,
    val services: List<Pair<MaintenanceServiceModel, List<PowerWrapper>>> = emptyList(),
    val error: String? = null
)

sealed class ApplyMaintenanceJobState {
    object Idle : ApplyMaintenanceJobState()
    object Loading : ApplyMaintenanceJobState()
    object Success : ApplyMaintenanceJobState()
    data class Error(val message: String) : ApplyMaintenanceJobState()
}

sealed class CancelMaintenanceJobState {
    object Idle : CancelMaintenanceJobState()
    object Loading : CancelMaintenanceJobState()
    object Success : CancelMaintenanceJobState()
    data class Error(val message: String) : CancelMaintenanceJobState()
}

