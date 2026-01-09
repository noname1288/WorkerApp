package com.example.workerapp.presentation.screens.notification_chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workerapp.data.ChatRepository
import com.example.workerapp.data.NotificationRepository
import com.example.workerapp.data.source.model.NotificationItemModel
import com.example.workerapp.data.source.model.RoomModel
import com.example.workerapp.utils.cached.UserSession
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationChatViewModel @Inject constructor(
    private val notificationRepository: NotificationRepository,
    private val chatRepository: ChatRepository
) : ViewModel() {

    private val _notificationUiState = MutableStateFlow<NotificationUiState>(NotificationUiState.Idle)
    val notificationUiState: StateFlow<NotificationUiState> = _notificationUiState

    private val _roomChat = MutableStateFlow<List<RoomModel>>(emptyList())
    val roomChat = _roomChat.asStateFlow()

    private val _listNoti = MutableStateFlow<List<NotificationItemModel>>(emptyList())
    val listNoti: StateFlow<List<NotificationItemModel>> = _listNoti

    val hasUnread = listNoti.map { list ->
        list.any { !it.isRead }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, false)

    fun fetchAllNotifications() {
        viewModelScope.launch {
            _notificationUiState.value = NotificationUiState.Loading

            val result = notificationRepository.getNotifications()
            result.onSuccess {
                _notificationUiState.value = NotificationUiState.Success(it)
                _listNoti.value = it
            }.onFailure {
                _notificationUiState.value =
                    NotificationUiState.Error(it.message ?: "An unknown error occurred")
            }
        }
    }

    fun markAsRead(notificationId: String) {
        viewModelScope.launch {
            val currentList = _listNoti.value
            // Cập nhật tạm thời để UI phản hồi nhanh
            _listNoti.value = currentList.map { item ->
                if (item.uid == notificationId) item.copy(isRead = true) else item
            }

            val result = notificationRepository.markNotificationAsRead(notificationId)
            result.onFailure {
                // Rollback nếu BE báo lỗi
                _listNoti.value = currentList
                _notificationUiState.value = NotificationUiState.Error(it.message ?: "Không thể đánh dấu đã đọc")
            }.onSuccess{

            }
        }
    }

    fun fetchAllConversations(){
        viewModelScope.launch {
            val currentUserUid = UserSession.uid

            if (currentUserUid.isNullOrEmpty()){
                return@launch
            }

            chatRepository.getAllConversations(currentUserUid)
                .collect{ _roomChat.value = it}
        }
    }

    override fun onCleared() {
        super.onCleared()
        // Cleanup listener nếu cần
    }
}

sealed class NotificationUiState {
    object Idle : NotificationUiState()
    object Loading : NotificationUiState()
    data class Success(val notifications: List<NotificationItemModel>) : NotificationUiState()
    data class Error(val message: String) : NotificationUiState()
}
