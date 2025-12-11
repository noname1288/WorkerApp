package com.example.workerapp.data

import com.example.workerapp.data.source.remote.dto.request.ChatbotRequest
import com.example.workerapp.data.source.remote.dto.response.GeoCodingResposne
import com.example.workerapp.presentation.screens.bot.ChatbotResponseUiModel

interface ChatbotRepository {
    suspend fun sendMessage(request: ChatbotRequest) : Result<ChatbotResponseUiModel>

    suspend fun getGeoCoding(lat: Double, lon: Double) : Result<GeoCodingResposne>
}
