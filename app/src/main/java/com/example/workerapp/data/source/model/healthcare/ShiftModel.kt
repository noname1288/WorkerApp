package com.example.workerapp.data.source.model.healthcare

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ShiftModel(
    var uid: String = "",
    var workingHour: Int = 0,
    var fee: Double = 0.0,
)
