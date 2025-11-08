package com.example.workerapp.data.source.remote.dto.request

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ResetPasswordRequest(
    val email: String,
    val code: String,
    val codeEnter: String,
    val newPassword: String,
    val confirmPassword: String
)
