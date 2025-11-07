package com.example.workerapp.data

import com.example.workerapp.data.source.model.NotificationItemModel

interface NotificationRepository {
    suspend fun getNotifications(): Result<List<NotificationItemModel>>

    suspend fun markNotificationAsRead(notificationId: String): Result<Unit>
}
