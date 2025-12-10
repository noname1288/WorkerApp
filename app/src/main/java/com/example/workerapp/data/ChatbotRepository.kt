package com.example.workerapp.data

import com.example.workerapp.data.source.remote.dto.request.ChatbotRequest
import com.example.workerapp.data.source.remote.dto.response.ChatbotResponse

interface ChatbotRepository {
    suspend fun sendMessage(request: ChatbotRequest) : Result<ChatbotResponse>
}
