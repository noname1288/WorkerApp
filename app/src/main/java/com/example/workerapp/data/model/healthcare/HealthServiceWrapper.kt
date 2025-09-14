package com.example.workerapp.data.model.healthcare

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class HealthServiceWrapper(
    var healthcareService: HealthcareServiceModel = HealthcareServiceModel(),
    var quantity: Int = 0,
)