package com.example.workerapp.data.source.remote.dto

import com.example.workerapp.data.source.remote.dto.response.ApplicationDto
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BaseApplicationResponse(
    val success: Boolean,
    val message: String?,
    val orders: List<ApplicationDto>
)

