package com.example.workerapp.data

import com.example.workerapp.data.source.model.NotificationItem

interface NotificationRepository {
    suspend fun getNotifications(): Result<List<NotificationItem>>
}
