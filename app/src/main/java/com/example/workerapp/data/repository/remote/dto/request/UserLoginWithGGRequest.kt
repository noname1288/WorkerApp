package com.example.workerapp.data.repository.remote.dto.request

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserLoginWithGGRequest(
    val idToken: String,
    val role: String = "worker"
)
