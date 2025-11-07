package com.example.workerapp.data.source

import com.example.workerapp.data.source.model.NotificationItemModel
import com.example.workerapp.data.source.remote.dto.NetworkResult

interface NotificationDataSource {

    /* *
    * Local
    * */
    interface Local{
        suspend fun saveNotifications(notifications: List<NotificationItemModel>)
        suspend fun getNotifications() : List<NotificationItemModel>
    }


    /* *
     * Remote
     */
    interface Remote{
        suspend fun getNotifications() : NetworkResult<List<NotificationItemModel>>

        suspend fun markNotificationAsRead(notificationId: String) : NetworkResult<Unit>
    }
}