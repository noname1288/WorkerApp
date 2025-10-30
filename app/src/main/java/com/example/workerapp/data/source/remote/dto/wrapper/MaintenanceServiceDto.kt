package com.example.workerapp.data.source.remote.dto.wrapper

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MaintenanceServiceDto(
    var uid: String = "",
    var powers: List<PowerDto> = emptyList()
)
