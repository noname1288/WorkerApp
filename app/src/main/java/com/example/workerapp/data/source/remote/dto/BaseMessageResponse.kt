package com.example.workerapp.data.source.remote.dto

import com.example.workerapp.data.source.model.MessageModel
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BaseMessageResponse(
    val success: Boolean,
    val message: String,
    val messages: List<MessageModel>
)
