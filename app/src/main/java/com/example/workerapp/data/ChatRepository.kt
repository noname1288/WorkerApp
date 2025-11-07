package com.example.workerapp.data

import com.example.workerapp.data.source.model.MessageModel
import com.example.workerapp.data.source.model.RoomModel
import com.example.workerapp.data.source.remote.dto.NetworkResult
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    suspend fun getAllConversations(currentUserId: String) : Flow<List<RoomModel>>
    suspend fun observeMessageByConversationId(conversationId: String) : Flow<MessageModel>
    suspend fun sendMessage(conversationId: String, messageModel: MessageModel) : NetworkResult<Unit>
}
