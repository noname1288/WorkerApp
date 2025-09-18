package com.example.workerapp.data.source.remote.dto.response

import com.example.workerapp.data.source.model.NotificationItem
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NotificationsResponse(
    val success: Boolean,
    val message: String,
    val notifications: List<NotificationItem>
)