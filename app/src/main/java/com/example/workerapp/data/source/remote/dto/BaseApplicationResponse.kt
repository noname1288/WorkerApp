package com.example.workerapp.data.source.remote.dto

import com.example.workerapp.data.source.model.base.JobModel1
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BaseApplicationResponse(
    val success: Boolean,
    val message: String,
    val orders: List<ApplicationWrapper>
)

@JsonClass(generateAdapter = true)
data class ApplicationWrapper(
    val uid: String,
    val job: JobModel1,
    val isReview: Boolean,
    val status: String,
    val createdAt: String,
    val serviceType: String,
)
