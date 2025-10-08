package com.example.workerapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workerapp.data.JobServiceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val jobServiceRepository: JobServiceRepository
) : ViewModel() {

    private val _homeUiState = MutableStateFlow<HomeUiState>(HomeUiState.Idle)
    val homeUiState: MutableStateFlow<HomeUiState> = _homeUiState

    fun fetchServices() {
        viewModelScope.launch {
            _homeUiState.value = HomeUiState.Loading

            try {
                val result1 = jobServiceRepository.getCleaningServices()
                result1.onSuccess {
                    _homeUiState.value = HomeUiState.Success("Fetched ${it.size} cleaning services")
                }.onFailure {
                    _homeUiState.value =
                        HomeUiState.Error(it.message ?: "Failed to fetch cleaning services")
                    return@launch
                }

                val result2 = jobServiceRepository.getHealthcareServices()
                result2.onSuccess {
                    _homeUiState.value = HomeUiState.Success("Fetched ${it.size} healthcare services")
                }.onFailure {
                    _homeUiState.value =
                        HomeUiState.Error(it.message ?: "Failed to fetch healthcare services")
                    return@launch
                }
            } catch (e: Exception) {
                _homeUiState.value = HomeUiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}

sealed class HomeUiState {
    object Idle : HomeUiState()
    object Loading : HomeUiState()
    data class Success(val data: String) : HomeUiState()
    data class Error(val message: String) : HomeUiState()
}
