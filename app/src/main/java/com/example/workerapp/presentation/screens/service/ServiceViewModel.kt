package com.example.workerapp.presentation.screens.service

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workerapp.data.source.remote.dto.NetworkResult
import com.example.workerapp.data.source.model.base.JobModel1
import com.example.workerapp.data.source.remote.JobRemoteImpl
import com.example.workerapp.utils.ServiceType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ServiceViewModel @Inject constructor(
    private val _jobRemoteImpl: JobRemoteImpl
) : ViewModel(){

    private val _uiState = MutableStateFlow<ServiceUIState>(ServiceUIState.Idle)
    val uiState: MutableStateFlow<ServiceUIState> = _uiState

    private val _serviceTypeState = MutableStateFlow(ServiceType.CleaningType)

    fun updateServiceType(newType: String){
        _serviceTypeState.value = newType
    }

    fun fetchData(){
        viewModelScope.launch {
            _uiState.value = ServiceUIState.Loading
            try {
                val result = when (_serviceTypeState.value){
                    ServiceType.CleaningType -> _jobRemoteImpl.getCleaningJobs()
                    ServiceType.HealthcareType -> _jobRemoteImpl.getHealthcareJobs()
//                    ServiceType.MaintenanceType -> _jobRemoteImpl.getMaintenanceJobs()
                    else -> _jobRemoteImpl.getCleaningJobs()
                }

                when(result){
                    is NetworkResult.Error -> {
                        _uiState.value = ServiceUIState.Error(result.message)
                    }
                    is NetworkResult.Success -> {
                        _uiState.value = ServiceUIState.Success(result.data)
                    }
                }
            }catch (e: Exception){
                _uiState.value = ServiceUIState.Error(e.message ?: "Something went wrong")
            }
        }
    }
}

sealed class ServiceUIState{
    data class Success(val jobs: List<JobModel1>) : ServiceUIState()
    data class Error(val message: String) : ServiceUIState()
    object Loading : ServiceUIState()
    object Idle : ServiceUIState()
}