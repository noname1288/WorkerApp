package com.example.workerapp.data.source.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NotificationItem(
    val uid: String = "",
    val jobID: String = "",
    val title: String = "",
    val content: String = "",
    val createdAt: String = "",
    var isRead: Boolean = false,
    val serviceType: String = "",
    var time: String = "",
)
