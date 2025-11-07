package com.example.workerapp.data.source.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass

@Entity(tableName = "notifications")
@JsonClass(generateAdapter = true)
data class NotificationItemModel(
    @PrimaryKey
    val uid: String = "",
    val title: String = "",
    val content: String = "",
    val createdAt: String = "",
    val isRead: Boolean = false,
)
