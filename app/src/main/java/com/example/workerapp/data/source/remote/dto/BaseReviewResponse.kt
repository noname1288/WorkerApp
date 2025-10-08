package com.example.workerapp.data.source.remote.dto

import com.example.workerapp.data.source.remote.dto.response.ReviewResponse
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BaseReviewResponse(
    val success: Boolean,
    val message: String,
    val experiences: List<ReviewResponse>
)
