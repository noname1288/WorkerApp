package com.example.workerapp.data.source.remote.dto.response

import com.google.gson.JsonElement
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class ChatbotResponse(
    val context: String?,
    val intent: String?,
    @Json(name = "session_id")
    val sessionId: String?,
    val jobs: List<ChatbotJobResponse>?,
    val metadata: MetaDataModel?
)


@JsonClass(generateAdapter = true)
data class MetaDataModel(
    val method: String?,

    // chỉ có trong job response
    @Json(name = "conversation_length")
    val conversationLength: Int?
)


@JsonClass(generateAdapter = true)
data class ChatbotJobResponse(
    val context: String,
    val createdAt: String,

    @Json(name = "jobID")
    val jobID: String,

    val lat: Double,

    val listDays: List<String>,
    val location: String,
    val lon: Double,
    val price: Int,
    val serviceType: String,
    val startTime: String,

    @Json(name = "userID")
    val userID: String,

    @Json(name = "similarity_score")
    val similarityScore: Double
)
