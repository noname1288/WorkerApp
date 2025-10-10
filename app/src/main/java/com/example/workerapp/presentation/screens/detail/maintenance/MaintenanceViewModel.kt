package com.example.workerapp.presentation.screens.detail.maintenance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workerapp.data.JobServiceRepository
import com.example.workerapp.data.source.model.maintenance.MaintenanceJobModel
import com.example.workerapp.data.source.remote.JobRemoteImpl
import com.example.workerapp.data.source.remote.dto.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MaintenanceViewModel @Inject constructor(
    private val jobRemoteImpl: JobRemoteImpl
): ViewModel(){

    private val _uiState = MutableStateFlow<MaintenanceDetailUIState>(MaintenanceDetailUIState.Idle)
    val uiState: MutableStateFlow<MaintenanceDetailUIState> = _uiState

    fun fetchJobDetail(uid: String){
        if (uid.isEmpty()){
            _uiState.value = MaintenanceDetailUIState.Error("Invalid job ID")
            return
        }

        viewModelScope.launch {
            _uiState.value = MaintenanceDetailUIState.Loading

            try {
                val result = jobRemoteImpl.getMaintenanceDetail(uid)

                when(result){
                    is NetworkResult.Error -> {
                        _uiState.value = MaintenanceDetailUIState.Error(result.message)
                    }
                    is NetworkResult.Success -> {
                        _uiState.value = MaintenanceDetailUIState.Success(result.data)
                    }
                }
            }catch (e : Exception){
                _uiState.value = MaintenanceDetailUIState.Error(e.message ?: "Unknown error")
            }
        }
    }
}

sealed class MaintenanceDetailUIState {
    object Loading : MaintenanceDetailUIState()
    object Idle : MaintenanceDetailUIState()
    data class Success(val maintenanceJob: MaintenanceJobModel) : MaintenanceDetailUIState()
    data class Error(val message: String) : MaintenanceDetailUIState()
}