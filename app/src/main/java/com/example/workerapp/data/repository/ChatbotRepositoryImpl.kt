package com.example.workerapp.data.repository

import com.example.workerapp.data.ChatbotRepository
import com.example.workerapp.data.source.ChatbotDataSource
import com.example.workerapp.data.source.remote.dto.NetworkResult
import com.example.workerapp.data.source.remote.dto.request.ChatbotRequest
import com.example.workerapp.data.source.remote.dto.response.ChatbotJobResponse
import com.example.workerapp.data.source.remote.dto.response.GeoCodingResposne
import com.example.workerapp.presentation.screens.bot.ChatbotResponseUiModel
import com.example.workerapp.utils.IntentType
import javax.inject.Inject

class ChatbotRepositoryImpl @Inject constructor(
    private val remote: ChatbotDataSource.Remote
) : ChatbotRepository {

    override suspend fun sendMessage(
        request: ChatbotRequest
    ): Result<ChatbotResponseUiModel> {
        val response = remote.sendMsg(request)

        when (response) {
            is NetworkResult.Error -> {
                return Result.failure(Exception("Fail to send message"))
            }

            is NetworkResult.Success -> {
                val body = response.data

                if (body.intent == IntentType.JobType) {
                    val message = body.context ?: "Something error"
                    val jobs = body.jobs ?: emptyList()
                    return Result.success(
                        ChatbotResponseUiModel.JobResponse(
                            text = message,
                            listJobs = jobs
                        )
                    )
                } else {
                    val message = body.context ?: "Something error"
                    return Result.success(ChatbotResponseUiModel.TextResponse(userUid = "chatbot", text = message))
                }
            }
        }

    }

    override suspend fun getGeoCoding(
        lat: Double,
        lon: Double
    ): Result<GeoCodingResposne> {
        val response = remote.getGeocoding(lat, lon)

        when (response) {
            is NetworkResult.Error -> {
                return Result.failure(Exception("Fail to load current location"))
            }

            is NetworkResult.Success -> {
                return Result.success(response.data)
            }
        }
    }

}

