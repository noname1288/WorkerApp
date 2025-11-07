package com.example.workerapp.data.source.remote.dto

import com.example.workerapp.data.source.model.RoomModel
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BaseConversationResponse(
    val success: Boolean,
    val message: String,
    val conversations: List<RoomModel>
)
