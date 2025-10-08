package com.example.workerapp.data.source.model.maintenance

import com.example.workerapp.utils.ServiceType
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MaintenanceServiceModel(
    val uid: String,
    val serviceName: String,
    val serviceType: String = ServiceType.MaintenanceType,
    val image: String,
    val maintenance: String,
    val powers: List<Power>,
)
