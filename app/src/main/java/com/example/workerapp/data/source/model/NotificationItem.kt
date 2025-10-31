package com.example.workerapp.data.source.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NotificationItem(
    val uid: String = "",
    val title: String = "",
    val content: String = "",
    val createdAt: String = "",
    val isRead: Boolean = false,
)
