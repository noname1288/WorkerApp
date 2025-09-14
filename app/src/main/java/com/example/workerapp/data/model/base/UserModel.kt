package com.example.workerapp.data.model.base

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserModel(
    val uid: String = "",
    val username: String = "",
    val gender: String = "",
    val dob: String = "",
    val avatar: String = "",
    val tel: String = "",
    val location: String = "",
    val email: String = "",
    val role: String = ""
)
