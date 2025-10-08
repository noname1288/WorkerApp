package com.example.workerapp.data.source.model.maintenance

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Power(
    val uid: String,
    val name: String,
    val price: Int,
    val priceAction: Int,
)
