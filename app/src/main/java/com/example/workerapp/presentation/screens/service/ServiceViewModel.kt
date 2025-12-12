package com.example.workerapp.presentation.screens.service

import androidx.compose.ui.res.painterResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workerapp.R
import com.example.workerapp.data.JobServiceRepository
import com.example.workerapp.data.source.model.base.JobModel1
import com.example.workerapp.data.source.model.maintenance.MaintenanceJobResponse
import com.example.workerapp.data.source.remote.JobRemoteImpl
import com.example.workerapp.data.source.remote.dto.NetworkResult
import com.example.workerapp.data.source.remote.dto.wrapper.MaintenanceServiceDto
import com.example.workerapp.utils.ServiceType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ServiceViewModel @Inject constructor(
    private val _jobRemoteImpl: JobRemoteImpl,
    private val _jobServiceRepository: JobServiceRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<ServiceUIState>(ServiceUIState.Idle)
    val uiState: MutableStateFlow<ServiceUIState> = _uiState

    private val _jobImageMap = MutableStateFlow<Map<String, String>>(emptyMap())
    val jobImageMap = _jobImageMap.asStateFlow()

    private val DEFAULT_MAINTAIN_IMAGE = R.drawable.img_maintain_service.toString()
    private val _serviceTypeState = MutableStateFlow(ServiceType.CleaningType)

    fun updateServiceType(newType: String) {
        _serviceTypeState.value = newType
    }

    fun fetchData() {
        viewModelScope.launch {
            _uiState.value = ServiceUIState.Loading
            try {
                val result = when (_serviceTypeState.value) {
                    ServiceType.CleaningType -> _jobRemoteImpl.getCleaningJobs()
                    ServiceType.HealthcareType -> _jobRemoteImpl.getHealthcareJobs()
                    ServiceType.MaintenanceType -> _jobRemoteImpl.getMaintenanceJobs()
                    else -> _jobRemoteImpl.getCleaningJobs()
                }

                when (result) {
                    is NetworkResult.Error -> {
                        _uiState.value = ServiceUIState.Error(result.message)
                    }

                    is NetworkResult.Success -> {
                        _uiState.value = ServiceUIState.Success(result.data)
                    }
                }
            } catch (e: Exception) {
                _uiState.value = ServiceUIState.Error(e.message ?: "Something went wrong")
            }
        }
    }

    fun loadMaintenanceJobImage(job: MaintenanceJobResponse) {
        // tránh load lại nếu đã có
        if (_jobImageMap.value.containsKey(job.uid)) return

        val services = job.services
        if (services.isNullOrEmpty()) {
            putImage(job.uid, DEFAULT_MAINTAIN_IMAGE)
            return
        }

        // nhiều service → image mặc định
        if (services.size > 1) {
            putImage(job.uid, DEFAULT_MAINTAIN_IMAGE)
            return
        }

        // chỉ 1 service → lấy image từ API
        viewModelScope.launch {
            _jobServiceRepository
                .getMaintenanceServiceByUid(services.first().uid)
                .onSuccess { data ->
                    putImage(job.uid, data.image)
                }
                .onFailure {
                    putImage(job.uid, DEFAULT_MAINTAIN_IMAGE)
                }
        }
    }

    private fun putImage(jobUid: String, image: String) {
        _jobImageMap.update { oldMap ->
            oldMap + (jobUid to image)
        }
    }

}

sealed class ServiceUIState {
    data class Success(val jobs: List<JobModel1>) : ServiceUIState()
    data class Error(val message: String) : ServiceUIState()
    object Loading : ServiceUIState()
    object Idle : ServiceUIState()
}