package com.example.workerapp.presentation.screens.notification_chat.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workerapp.data.ChatRepository
import com.example.workerapp.data.source.model.MessageModel
import com.example.workerapp.data.source.remote.dto.NetworkResult
import com.example.workerapp.utils.cached.UserSession
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatDetailViewModel @Inject constructor(
    private val chatRepository: ChatRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<ChatDetailUiState>(ChatDetailUiState.Idle)
    val uiState = _uiState.asStateFlow()

    private val _messageList = MutableStateFlow<List<MessageModel>>(emptyList())
    val messageList = _messageList.asStateFlow()

    fun observeMessages(conversationId: String){
        viewModelScope.launch {
            chatRepository.observeMessageByConversationId(conversationId).collect { newMsg ->
                _messageList.update { oldList -> oldList + newMsg }
            }
        }
    }

    fun sendMessage(conversationId: String, receiverId: String, content: String) {
        viewModelScope.launch {
            _uiState.value = ChatDetailUiState.Loading

            val currentUserUid = UserSession.uid
            if (currentUserUid == null) {
                _uiState.value = ChatDetailUiState.Error("User not logged in")
                return@launch
            }

            val currentTimestamp = System.currentTimeMillis()
            val msgId = "msg_" + currentTimestamp.toString()
            val messageModel = MessageModel(
                id = msgId,
                receiverId = receiverId,
                senderId = currentUserUid, // Thay bằng user hiện tại
                message = content,
                timestamp =currentTimestamp
            )

            val result = chatRepository.sendMessage(conversationId, messageModel)

            when(result){
                is NetworkResult.Error -> {
                    _uiState.value = ChatDetailUiState.Error(result.message ?: "Error sending message")
                }
                is NetworkResult.Success -> {
                    _uiState.value = ChatDetailUiState.Success
                }
            }

        }
    }
}

sealed class ChatDetailUiState {
    object Idle : ChatDetailUiState()
    object Loading : ChatDetailUiState()
    object Success : ChatDetailUiState()
    data class Error(val message: String) : ChatDetailUiState()
}



