package com.example.workerapp.data.source.remote.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TokenResponse<T>(
    val success: Boolean,
    val message: String,
    val data: T?
)
