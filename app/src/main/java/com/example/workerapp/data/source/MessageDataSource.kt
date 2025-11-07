package com.example.workerapp.data.source

import com.example.workerapp.data.source.model.MessageModel
import com.example.workerapp.data.source.model.RoomModel
import com.example.workerapp.data.source.remote.dto.NetworkResult
import kotlinx.coroutines.flow.Flow

interface MessageDataSource {
    /* *
    * Local
    * */

    /* *
    * Remote
    * */
    interface Remote{
        suspend fun getAllConversations(currentUserUid: String) : Flow<List<RoomModel>>
        suspend fun observeMessages(conversationId: String) : Flow<MessageModel>
        suspend fun sendMsg(conversationId: String, messageModel: MessageModel) : NetworkResult<Unit>

        suspend fun deleteMessageListener(conversationId: String)
    }
}