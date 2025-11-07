package com.example.workerapp.data.source.remote.dto.request

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SendMessageRequest(
    val senderId: String,
    val receiverId: String,
    val message: String,
    val type: String
)
