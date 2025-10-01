package com.example.workerapp.data.source.remote.dto.response

import com.example.workerapp.data.source.model.base.UserModel
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserWrapperResponse(
    var user: UserModel = UserModel(),
    var token: String = "",
    var refreshToken: String = "",
)
