package com.example.workerapp.data.source.remote.dto.response

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CancelApplicationResponse(
    val success: Boolean,
    val message: String,
    val updatedOrder: CancelApplicationWrapper?
)

@JsonClass(generateAdapter = true)
data class CancelApplicationWrapper(
    val uid: String = "",
    val workerID: String = "",
    val jobID: String = "",
    val serviceType: String = "",
    val createdAt: String = "",
    val isReview: Boolean = false,
    val status: String = "",
)
