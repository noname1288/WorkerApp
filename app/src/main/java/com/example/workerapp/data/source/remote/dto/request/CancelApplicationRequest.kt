package com.example.workerapp.data.source.remote.dto.request

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CancelApplicationRequest(
    @Json(name = "uid")
    val applicationUid: String, // applicationUid
    val status : String,
)
