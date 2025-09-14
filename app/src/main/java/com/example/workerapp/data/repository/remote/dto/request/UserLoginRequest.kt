package com.example.workerapp.data.repository.remote.dto.request

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserLoginRequest(
    val email: String,
    val password: String
)
