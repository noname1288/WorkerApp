package com.example.workerapp.data.repository.remote.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BaseJobResponse <T>(
    val success: Boolean,
    val message: String,
    val job: T?
)