package com.example.workerapp.ui.detail.cleaning

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workerapp.data.source.remote.NetworkResult
import com.example.workerapp.data.source.remote.dto.request.ApplicationRequest
import com.example.workerapp.data.source.remote.model.cleaning.CleaningJobModel1
import com.example.workerapp.data.source.remote.JobRemoteImpl
import com.example.workerapp.utils.ServiceType
import com.example.workerapp.utils.cached.UserSession
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CleaningViewModel : ViewModel() {
    private val cleaningRepository = JobRemoteImpl.getInstance()

    private val _uiState = MutableStateFlow<CleaningUiState>(CleaningUiState.Idle)
    val uiState: StateFlow<CleaningUiState> = _uiState

    private val _applyState = MutableStateFlow(false)
    val applyState: StateFlow<Boolean> = _applyState

    fun fetchJobDetail(uid: String) {
        if (uid.isEmpty()) {
            _uiState.value = CleaningUiState.Error("Invalid job ID")
            return
        }
        viewModelScope.launch {
            _uiState.value = CleaningUiState.Loading
            when (val result = cleaningRepository.getCleaningDetail(uid)) {
                is NetworkResult.Success -> {
                    _uiState.value = CleaningUiState.Success(result.data)
                }

                is NetworkResult.Error -> {
                    _uiState.value = CleaningUiState.Error(result.message)
                }
            }
        }
    }

    fun applyToJob(uid: String) {
        if (uid.isEmpty()) {
            _uiState.value = CleaningUiState.Error("Invalid job ID")
            return
        }
        viewModelScope.launch {
            try {
                val request = ApplicationRequest(
                    workerID = UserSession.uid,
                    jobID = uid,
                    serviceType = ServiceType.CleaningType
                )

                Log.d("CleaningViewModel", "Applying with request: $request")

                val result = cleaningRepository.applyForJob(request)
                when (result) {
                    is NetworkResult.Success -> {
                        _applyState.value = true
                    }
                    is NetworkResult.Error -> {
                        _uiState.value = CleaningUiState.Error(result.message)
                    }
                }
            } catch (e: Exception) {
                _applyState.value = true
                _uiState.value = CleaningUiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}

sealed class CleaningUiState {
    object Loading : CleaningUiState()
    object Idle : CleaningUiState()
    data class Success(val job: CleaningJobModel1) : CleaningUiState()
    data class Error(val message: String) : CleaningUiState()
}

