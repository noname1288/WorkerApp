package com.example.workerapp.data.source.remote.dto.response

import com.example.workerapp.data.source.model.maintenance.PowerModel
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MaintenanceServiceResponse(
    val uid: String,
    val serviceName: String,
    val serviceType: String ,
    val image: String,
    val maintenance: String,
    val powers: List<PowerModel>
)
