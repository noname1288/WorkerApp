package com.example.workerapp.data.repository

import com.example.workerapp.data.ChatbotRepository
import com.example.workerapp.data.source.ChatbotDataSource
import com.example.workerapp.data.source.remote.dto.NetworkResult
import com.example.workerapp.data.source.remote.dto.request.ChatbotRequest
import com.example.workerapp.data.source.remote.dto.response.ChatbotResponse
import com.example.workerapp.data.source.remote.dto.response.GeoCodingResposne
import javax.inject.Inject

class ChatbotRepositoryImpl @Inject constructor(
    private val remote: ChatbotDataSource.Remote
) : ChatbotRepository {

    override suspend fun sendMessage(
        request: ChatbotRequest
    ): Result<ChatbotResponse> {
        val response = remote.sendMsg(request)

        when(response){
            is NetworkResult.Error ->{
                return Result.failure(Exception("Fail to send message"))
            }
            is NetworkResult.Success -> {
                return Result.success(response.data)
            }
        }

    }

    override suspend fun getGeoCoding(
        lat: Double,
        lon: Double
    ): Result<GeoCodingResposne> {
        val response = remote.getGeocoding(lat, lon)

        when(response){
            is NetworkResult.Error -> {
                return Result.failure(Exception("Fail to load current location"))
            }
            is NetworkResult.Success -> {
                return Result.success(response.data)
            }
        }
    }

}
