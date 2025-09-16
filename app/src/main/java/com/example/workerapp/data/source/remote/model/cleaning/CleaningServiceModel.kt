package com.example.workerapp.data.source.remote.model.cleaning

import com.example.workerapp.utils.ServiceType
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CleaningServiceModel(
    var uid: String = "",
    @Json(name = "tasks")
    var duties: List<String> = emptyList(),

    @Json(name = "image")
    var imageUrl: String = "",
    val serviceType: String = ServiceType.CleaningType,
    var serviceName: String = ""
)
