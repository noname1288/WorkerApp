package com.example.workerapp.data.source.remote.dto.request

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserRegisterRequest (
    val  username: String,
    val email: String,
    val password: String,
    val avatar: String?,
    val role: String = "worker"
)
