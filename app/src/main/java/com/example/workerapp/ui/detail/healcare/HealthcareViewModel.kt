package com.example.workerapp.ui.detail.healcare

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workerapp.data.model.healthcare.HealthcareJobModel
import com.example.workerapp.data.repository.remote.repository.JobRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class HealthcareViewModel : ViewModel() {
    val repository = JobRepositoryImpl.getInstance()

    private val _uiState = MutableStateFlow<HealthcareUiState>(HealthcareUiState.Idle)
    val uiState: MutableStateFlow<HealthcareUiState> = _uiState

    init {
        fetchJobDetail()
    }

    fun fetchJobDetail() {
        viewModelScope.launch {
            _uiState.value = HealthcareUiState.Loading
            try {
                val result = repository.getHealthcareDetail("1mLLN663AiCu0ymv421B")
                when (result) {
                    is com.example.workerapp.data.repository.remote.NetworkResult.Success -> {
                        _uiState.value = HealthcareUiState.Success(result.data)
                    }
                    is com.example.workerapp.data.repository.remote.NetworkResult.Error -> {
                        _uiState.value = HealthcareUiState.Error(result.message)
                    }
                }
            } catch (e: Exception) {
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