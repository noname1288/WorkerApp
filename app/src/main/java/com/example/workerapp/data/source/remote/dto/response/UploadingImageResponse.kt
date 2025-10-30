package com.example.workerapp.data.source.remote.dto.response

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UploadingImageResponse(
    val success: Boolean,
    val message: String,
    val url: String,
)
