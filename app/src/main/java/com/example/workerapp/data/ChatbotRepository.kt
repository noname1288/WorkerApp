package com.example.workerapp.data

import com.example.workerapp.data.source.remote.dto.request.ChatbotRequest
import com.example.workerapp.data.source.remote.dto.response.ChatbotResponse
import com.example.workerapp.data.source.remote.dto.response.GeoCodingResposne

interface ChatbotRepository {
    suspend fun sendMessage(request: ChatbotRequest) : Result<ChatbotResponse>

    suspend fun getGeoCoding(lat: Double, lon: Double) : Result<GeoCodingResposne>
}
