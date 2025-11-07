package com.example.workerapp.data.source.local

import com.example.workerapp.data.source.NotificationDataSource
import com.example.workerapp.data.source.local.room.NotificationDao
import com.example.workerapp.data.source.model.NotificationItemModel
import javax.inject.Inject

class NotificationLocalImpl @Inject constructor(
    private val notificationDao: NotificationDao
) : NotificationDataSource.Local {
    override suspend fun saveNotifications(notifications: List<NotificationItemModel>) {
        notificationDao.insertNotifications(notifications)
    }

    override suspend fun getNotifications(): List<NotificationItemModel> =
        notificationDao.getAllNotifications()
}
