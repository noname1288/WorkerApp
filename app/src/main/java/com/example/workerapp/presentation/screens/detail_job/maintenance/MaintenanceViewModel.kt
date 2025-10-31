package com.example.workerapp.presentation.screens.detail_job.maintenance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workerapp.data.JobServiceRepository
import com.example.workerapp.data.source.model.maintenance.MaintenanceJobResponse
import com.example.workerapp.data.source.model.maintenance.MaintenanceServiceModel
import com.example.workerapp.data.source.model.maintenance.PowerWrapper
import com.example.workerapp.data.source.remote.JobRemoteImpl
import com.example.workerapp.data.source.remote.dto.NetworkResult
import com.example.workerapp.data.source.remote.dto.request.ApplicationRequest
import com.example.workerapp.data.source.remote.dto.wrapper.MaintenanceServiceDto
import com.example.workerapp.utils.ServiceType
import com.example.workerapp.utils.cached.UserSession
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MaintenanceViewModel @Inject constructor(
    private val jobRemoteImpl: JobRemoteImpl,
    private val jobServiceRepository: JobServiceRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<MaintenanceDetailUIState>(MaintenanceDetailUIState.Idle)
    val uiState: MutableStateFlow<MaintenanceDetailUIState> = _uiState

    private val _applyState = MutableStateFlow<Boolean?>(null)
    val applyState: StateFlow<Boolean?> = _applyState

    fun updateApplyState(value: Boolean?) {
        _applyState.value = value
    }

    fun fetchJobDetail(uid: String) {
        if (uid.isEmpty()) {
            _uiState.value = MaintenanceDetailUIState.Error("Invalid job ID")
            return
        }

        viewModelScope.launch {
            _uiState.value = MaintenanceDetailUIState.Loading

            try {
                val result = jobRemoteImpl.getMaintenanceDetail(uid)

                when (result) {
                    is NetworkResult.Error -> {
                        _uiState.value = MaintenanceDetailUIState.Error(result.message)
                    }

                    is NetworkResult.Success -> {
                        val jobData = result.data
                        val serviceWrapper = jobData.services

                        val serviceData = fetchMaintenanceService(serviceWrapper)

                        _uiState.value = MaintenanceDetailUIState.Success(jobData, serviceData)
                    }
                }
            } catch (e: Exception) {
                _uiState.value = MaintenanceDetailUIState.Error(e.message ?: "Unknown error")
            }
        }
    }

    suspend fun fetchMaintenanceService(serviceDtoList: List<MaintenanceServiceDto>): List<Pair<MaintenanceServiceModel, List<PowerWrapper>>> {
        val mapData = mutableListOf<Pair<MaintenanceServiceModel, List<PowerWrapper>>>()

        for (item in serviceDtoList) {
            val service = jobServiceRepository.getMaintenanceServiceByUid(item.uid)
            val powerList = mutableListOf<PowerWrapper>()

            item.powers.map { index ->
                val powerModelResult = jobServiceRepository.getPowerModelByUid(index.uid)

                val powerWrapper = powerModelResult.onSuccess {
                    powerList.add(
                        PowerWrapper(
                            it.uid,
                            it.name,
                            it.price,
                            it.priceAction,
                            index.quantity,
                            index.quantityAction
                        )
                    )
                }.onFailure {
                    throw RuntimeException("Can't fetch power model from local storage")
                }
            }

            mapData.add(Pair(service.getOrThrow(), powerList))
        }

        return mapData.toList()
    }

    fun applyToJob(uid: String) {
        if (uid.isEmpty()) {
            _uiState.value = MaintenanceDetailUIState.Error("Invalid job ID")
            return
        }
        viewModelScope.launch {
            _uiState.value = MaintenanceDetailUIState.Loading

            try {
                val request = ApplicationRequest(
                    workerID = UserSession.uid,
                    jobID = uid,
                    serviceType = ServiceType.MaintenanceType
                )

                val result = jobRemoteImpl.applyForJob(request)
                when (result) {
                    is NetworkResult.Success -> {
                        _applyState.value = true
                    }

                    is NetworkResult.Error -> {
                        _applyState.value = false
                        _uiState.value = MaintenanceDetailUIState.Error(result.message)
                    }
                }
            } catch (e: Exception) {
                _applyState.value = true
                _uiState.value = MaintenanceDetailUIState.Error(e.message ?: "Unknown error")
            }
        }
    }
}

sealed class MaintenanceDetailUIState {
    object Loading : MaintenanceDetailUIState()
    object Idle : MaintenanceDetailUIState()
    data class Success(
        val maintenanceJob: MaintenanceJobResponse,
        val serviceData: List<Pair<MaintenanceServiceModel, List<PowerWrapper>>>
    ) : MaintenanceDetailUIState()

    data class Error(val message: String) : MaintenanceDetailUIState()
}
