package com.example.workerapp.data.repository.remote.dto.wrapper

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class HealthServiceWrapper(
    var serviceID: String = "",
    var quantity: Int = 0,
)