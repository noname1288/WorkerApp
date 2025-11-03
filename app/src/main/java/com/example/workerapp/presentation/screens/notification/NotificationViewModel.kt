package com.example.workerapp.presentation.screens.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workerapp.data.NotificationRepository
import com.example.workerapp.data.source.model.NotificationItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
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

    val hasUnread = listItems.map { list ->
        list.any { !it.isRead }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, false)

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

    fun markAsRead(notificationId: String) {
        viewModelScope.launch {
            val currentList = _listItems.value
            // Cập nhật tạm thời để UI phản hồi nhanh
            _listItems.value = currentList.map { item ->
                if (item.uid == notificationId) item.copy(isRead = true) else item
            }

            val result = notificationRepository.markNotificationAsRead(notificationId)
            result.onFailure {
                // Rollback nếu BE báo lỗi
                _listItems.value = currentList
                _uiState.value = NotificationUiState.Error(it.message ?: "Không thể đánh dấu đã đọc")
            }.onSuccess{

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
