package com.example.workerapp.data.source.remote.dto.wrapper

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MaintenanceServiceWrapper(
    var uid: String = "",
    var powers: List<PowerWrapper> = emptyList()
)
