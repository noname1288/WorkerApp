package com.example.workerapp.data.source.remote.model.healthcare

import com.example.workerapp.utils.ServiceType
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class HealthcareServiceModel(
    var uid: String = "",
    var serviceType: String = ServiceType.HealthcareType,
    var serviceName: String = "",
    var duties: List<String> = emptyList(),
    val excludedTasks: List<String> = emptyList(),
    @Json(name = "image")
    var imageUrl: String = "",
)
