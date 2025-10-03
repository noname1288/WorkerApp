package com.example.workerapp.presentation.screens.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workerapp.data.NotificationRepository
import com.example.workerapp.data.repository.NotificationRepositoryImpl
import com.example.workerapp.data.source.model.NotificationItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val notificationRepository: NotificationRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<NotificationUiState>(NotificationUiState.Idle)
    val uiState: StateFlow<NotificationUiState> = _uiState

    private val _listItems = MutableStateFlow<List<NotificationItem>>(emptyList())
    val listItems: StateFlow<List<NotificationItem>> = _listItems

    fun fetchAllNotifications() {
        viewModelScope.launch {
            _uiState.value = NotificationUiState.Loading

            val result = notificationRepository.getNotifications()
            result.onSuccess {
                _uiState.value = NotificationUiState.Success(it)
                _listItems.value = it
            }.onFailure {
                _uiState.value =
                    NotificationUiState.Error(it.message ?: "An unknown error occurred")
            }
        }
    }
}

sealed class NotificationUiState {
    object Idle : NotificationUiState()
    object Loading : NotificationUiState()
    data class Success(val notifications: List<NotificationItem>) : NotificationUiState()
    data class Error(val message: String) : NotificationUiState()
}
