package com.example.workerapp.data.source.remote.dto.wrapper

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PowerDto(
    var uid: String = "",
    var quantity: Int = 0,
    var quantityAction: Int = 0,
)
