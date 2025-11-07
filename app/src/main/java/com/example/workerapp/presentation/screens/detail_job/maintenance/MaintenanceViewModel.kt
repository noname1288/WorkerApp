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

    private val _uiState = MutableStateFlow(MaintenanceUiState())
    val uiState: StateFlow<MaintenanceUiState> = _uiState

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
                val result = jobRemoteImpl.getMaintenanceDetail(uid)
                when (result) {
                    is NetworkResult.Success -> {
                        val jobData = result.data
                        val serviceData = fetchMaintenanceService(jobData.services)
                        _uiState.value = MaintenanceUiState(
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
                    serviceType = ServiceType.MaintenanceType
                )

                val result = jobRemoteImpl.applyForJob(request)
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

data class MaintenanceUiState(
    val isLoading: Boolean = false,
    val job: MaintenanceJobResponse? = null,
    val services: List<Pair<MaintenanceServiceModel, List<PowerWrapper>>> = emptyList(),
    val error: String? = null
)

