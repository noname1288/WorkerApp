package com.example.workerapp.data.source.remote.dto.response

import com.example.workerapp.data.source.remote.dto.wrapper.ReviewWrapperLayer
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ReviewResponse(
    val CLEANING: ReviewWrapperLayer?,
    val HEALTHCARE: ReviewWrapperLayer?,
    val MAINTENANCE: ReviewWrapperLayer?,
)
