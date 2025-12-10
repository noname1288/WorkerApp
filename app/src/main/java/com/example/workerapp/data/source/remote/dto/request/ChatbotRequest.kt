package com.example.workerapp.data.source.remote.dto.request

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ChatbotRequest(
    val query: String,
    val reference: Reference
)

@JsonClass(generateAdapter = true)
data class Reference(
    val location: Location
)

@JsonClass(generateAdapter = true)
data class Location(
    val lat: Double,
    val lon: Double,
    val name: String
)
