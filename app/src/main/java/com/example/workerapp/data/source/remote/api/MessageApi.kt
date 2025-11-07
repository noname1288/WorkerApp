package com.example.workerapp.data.source.remote.api

import com.example.workerapp.data.source.remote.dto.BaseConversationResponse
import com.example.workerapp.data.source.remote.dto.BaseMessageResponse
import com.example.workerapp.data.source.remote.dto.request.SendMessageRequest
import com.example.workerapp.data.source.remote.dto.response.SendMessageResponse
import com.example.workerapp.utils.annotation.AuthRequired
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface MessageApi {
    @AuthRequired
    @GET("chat/conversations")
    suspend fun getAllConversations(): Response<BaseConversationResponse>

    @AuthRequired
    @GET("chat/messages/{roomId}")
    suspend fun getConversationById(
        @Path("roomId") roomId: String
    ): Response<BaseMessageResponse>

    @AuthRequired
    @POST("chat/send")
    suspend fun sendMessage(
        @Body request: SendMessageRequest
    ) : Response<SendMessageResponse>

}
