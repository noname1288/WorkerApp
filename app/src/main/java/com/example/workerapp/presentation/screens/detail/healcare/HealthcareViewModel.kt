package com.example.workerapp.ui.detail.healcare

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workerapp.data.source.remote.dto.NetworkResult
import com.example.workerapp.data.source.remote.dto.request.ApplicationRequest
import com.example.workerapp.data.source.model.healthcare.HealthcareJobModel
import com.example.workerapp.data.source.remote.JobRemoteImpl
import com.example.workerapp.utils.ServiceType
import com.example.workerapp.utils.cached.UserSession
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HealthcareViewModel : ViewModel() {
    val healthcareRepository = JobRemoteImpl.getInstance()

    private val _uiState = MutableStateFlow<HealthcareUiState>(HealthcareUiState.Idle)
    val uiState: MutableStateFlow<HealthcareUiState> = _uiState

    private val _applyState = MutableStateFlow<Boolean?>(null)
    val applyState: StateFlow<Boolean?> = _applyState

    fun updateApplyState(value: Boolean?){
        _applyState.value = value
    }

    fun fetchJobDetail(uid: String) {
        if (uid.isEmpty()){
            _uiState.value = HealthcareUiState.Error("Invalid job ID")
            return
        }
        viewModelScope.launch {
            _uiState.value = HealthcareUiState.Loading
            try {
                val result = healthcareRepository.getHealthcareDetail(uid)
                when (result) {
                    is NetworkResult.Success -> {
                        _uiState.value = HealthcareUiState.Success(result.data)
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

                val result = healthcareRepository.applyForJob(request)
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
    data class Success(val data: HealthcareJobModel) : HealthcareUiState()
    data class Error(val message: String) : HealthcareUiState()
}