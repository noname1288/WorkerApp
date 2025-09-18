package com.example.workerapp.data.source.model.cleaning

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.workerapp.utils.ServiceType
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Entity(tableName = "cleaning_service")
@JsonClass(generateAdapter = true)
data class CleaningServiceModel(
    @PrimaryKey var uid: String = "",

    @Json(name = "tasks")
    var duties: List<String> = emptyList(),

    @Json(name = "image")
    var imageUrl: String = "",
    val serviceType: String = ServiceType.CleaningType,
    var serviceName: String = ""
)
