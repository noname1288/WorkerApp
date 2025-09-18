package com.example.workerapp.data.source.model.cleaning

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DurationModel(
    var uid: String = "",
    var workingHour: Int = 0,
    var fee: Double = 0.0,
    var description: String = ""
)