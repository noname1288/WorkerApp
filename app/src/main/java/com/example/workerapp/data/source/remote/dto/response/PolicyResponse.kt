package com.example.workerapp.data.source.remote.dto.response

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PolicyResponse(
    val markdownContent : String?,
    val htmlContent: String?
)
