package com.example.workerapp.data.source.remote.dto.wrapper

import com.example.workerapp.data.source.model.ReviewModel
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ReviewWrapperLayer(
    val rating: Double,
    val reviews: List<ReviewModel>
)
