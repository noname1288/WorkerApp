package com.example.workerapp.data.source.remote.dto.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ChatbotResponse(
    val context: String?,
    val intent: String?,
    @Json(name = "session_id")
    val sessionId: String?,
    val jobs: List<ChatbotJobResponse>?,
    val metadata: MetaDataModel?,
)

@JsonClass(generateAdapter = true)
data class MetaDataModel(
    val method: String,
    val type: String
)

@JsonClass(generateAdapter = true)
data class ChatbotJobResponse(
    val context: String,
    val createdAt: String,
    val jobID: String,
    val lat: Double,
    val listDays: List<String>,
    val location: String,
    val lon: Double,
    val price: Int,
    val serviceType: String,
    val similarity_score: Double,
    val startTime: String,
    val userID: String
)