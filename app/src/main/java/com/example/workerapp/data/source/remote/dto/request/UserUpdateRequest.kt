package com.example.workerapp.data.source.remote.dto.request

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserUpdateRequest(
    val uid: String,
    val username: String,
    val gender: String,
    val dob: String,
    var avatar: String,
    var tel: String,
    var location: String,
    val email: String,
    val provider: String,
    val role: String,
)
