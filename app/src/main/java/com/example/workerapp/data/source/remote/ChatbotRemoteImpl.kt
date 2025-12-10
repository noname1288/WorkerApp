package com.example.workerapp.data.source.remote

import com.example.workerapp.data.source.ChatbotDataSource
import com.example.workerapp.data.source.remote.api.ChatbotApi
import com.example.workerapp.data.source.remote.dto.NetworkResult
import com.example.workerapp.data.source.remote.dto.request.ChatbotRequest
import com.example.workerapp.data.source.remote.dto.response.ApiErrorResponse
import com.example.workerapp.data.source.remote.dto.response.ChatbotResponse
import com.squareup.moshi.Moshi
import javax.inject.Inject

class ChatbotRemoteImpl @Inject constructor(
    private val chatbotApi: ChatbotApi,
    moshi: Moshi
) : ChatbotDataSource.Remote {

    private val errorAdapter = moshi.adapter(ApiErrorResponse::class.java)

    override suspend fun sendMsg(request: ChatbotRequest): NetworkResult<ChatbotResponse> {
        return try {
            val response = chatbotApi.sendMessage(request)

            if (response.isSuccessful){
                val body = response.body()

                if (body != null){
                    NetworkResult.Success(body)
                }else {
                    NetworkResult.Error("Empty response body")
                }
            }else {
                val errorMessage = response.errorBody()?.string()
                    ?.let { json -> errorAdapter.fromJson(json)?.error }
                    ?: response.message()
                    ?: "Request failed with status code ${response.code()}"

                NetworkResult.Error(errorMessage)
            }
        } catch (e: Exception) {
            NetworkResult.Error(e.localizedMessage ?: "Unexpected error occurred")
        }
    }
}
