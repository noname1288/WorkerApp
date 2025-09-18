package com.example.workerapp.data.source.model.healthcare

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.workerapp.utils.ServiceType
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Entity(tableName = "healthcare_service")
@JsonClass(generateAdapter = true)
data class HealthcareServiceModel(
    @PrimaryKey
    var uid: String = "",
    var serviceType: String = ServiceType.HealthcareType,
    var serviceName: String = "",
    var duties: List<String> = emptyList(),
    val excludedTasks: List<String> = emptyList(),
    @Json(name = "image")
    var imageUrl: String = "",
)
