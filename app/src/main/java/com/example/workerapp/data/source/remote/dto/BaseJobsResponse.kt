package com.example.workerapp.data.source.remote.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class BaseJobsResponse<T>(
    val success: Boolean,
    val message: String,
    val jobs: T?
)
