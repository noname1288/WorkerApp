package com.example.workerapp.data.source.remote.api

import com.example.workerapp.data.source.remote.dto.request.ChatbotRequest
import com.example.workerapp.data.source.remote.dto.response.ChatbotResponse
import com.example.workerapp.utils.annotation.AuthRequired
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ChatbotApi {
    @AuthRequired
    @POST("/api/chatbot")
    suspend fun sendMessage(
        @Body request: ChatbotRequest
    ): Response<ChatbotResponse>
}
