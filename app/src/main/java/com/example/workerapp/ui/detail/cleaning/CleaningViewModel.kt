package com.example.workerapp.ui.detail.cleaning

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workerapp.data.model.cleaning.CleaningJobModel1
import com.example.workerapp.data.repository.remote.NetworkResult
import com.example.workerapp.data.repository.remote.repository.JobRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CleaningViewModel : ViewModel() {
    private val tag = "CleaningViewModel"

    private val cleaningRepository = JobRepositoryImpl.getInstance()

    private val _uiState = MutableStateFlow<CleaningUiState>(CleaningUiState.Idle)
    val uiState: StateFlow<CleaningUiState> = _uiState

    init {
        fetchJobDetail()
    }

    fun fetchJobDetail() {
        viewModelScope.launch {
            _uiState.value = CleaningUiState.Loading
            when (val result = cleaningRepository.getCleaningDetail("67Yf1lIOiL6ot8FZFIZM")) {
                is NetworkResult.Success -> {
                    _uiState.value = CleaningUiState.Success(result.data)
                }

                is NetworkResult.Error -> {
                    _uiState.value = CleaningUiState.Error(result.message)
                }
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

