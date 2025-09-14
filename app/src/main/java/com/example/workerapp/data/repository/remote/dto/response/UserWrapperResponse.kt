package com.example.workerapp.data.repository.remote.dto.response

import com.example.workerapp.data.model.base.UserModel
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserWrapperResponse(
    var user: UserModel = UserModel(),
    var token: String = "",
)
