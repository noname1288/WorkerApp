package com.example.workerapp.data.repository.remote.dto

import com.example.workerapp.data.repository.remote.dto.response.UserWrapperResponse
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BaseUserResponse(
    val success: Boolean,
    val message: String,
    val data: UserWrapperResponse?,
)
