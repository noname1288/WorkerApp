package com.example.workerapp.data.source.model.maintenance

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MaintenanceJobModel(
    val uid: String
)
