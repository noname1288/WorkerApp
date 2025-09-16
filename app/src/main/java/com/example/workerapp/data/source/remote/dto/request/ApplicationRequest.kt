package com.example.workerapp.data.source.remote.dto.request

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApplicationRequest(
    val workerID: String?,
    val jobID: String,
    val serviceType: String,
)
