package com.example.workerapp.data.source.remote.dto.response

import com.example.workerapp.data.source.model.base.UserModel
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserUpdateResponse(
    val success: Boolean,
    val message: String,
    val user: UserModel
)
