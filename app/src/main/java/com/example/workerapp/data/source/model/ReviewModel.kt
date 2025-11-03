package com.example.workerapp.data.source.model

import com.example.workerapp.data.source.model.base.UserModel
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ReviewModel(
    val uid: String,
    val user: UserModel,
    val rating: Double,
    val comment: String,
)
