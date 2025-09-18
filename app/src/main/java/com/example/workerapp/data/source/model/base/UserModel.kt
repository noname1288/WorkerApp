package com.example.workerapp.data.source.model.base

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserModel(
    val uid: String = "",
    val username: String = "",
    val gender: String = "",
    val dob: String = "",
    var avatar: String = "",
    var tel: String = "",
    var location: String = "",
    val email: String = "",
    val provider:String = "",
    var emailVerified: Boolean = false,
    var requiresProfileUpdate: Boolean = false,
    val role: String = "",
    var lastLogin: String = "",
)
