package com.example.workerapp.data.repository

import com.example.workerapp.data.ChatRepository
import com.example.workerapp.data.source.MessageDataSource
import com.example.workerapp.data.source.model.MessageModel
import com.example.workerapp.data.source.model.RoomModel
import com.example.workerapp.data.source.remote.dto.NetworkResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val remote: MessageDataSource.Remote
) : ChatRepository {
    override suspend fun getAllConversations(currentUserId: String): Flow<List<RoomModel>> =
        remote.getAllConversations(currentUserId)

    override suspend fun observeMessageByConversationId(conversationId: String): Flow<MessageModel> =
        remote.observeMessages(conversationId)

    override suspend fun sendMessage(
        conversationId: String,
        messageModel: MessageModel
    ): NetworkResult<Unit> = remote.sendMsg(conversationId, messageModel)

}
