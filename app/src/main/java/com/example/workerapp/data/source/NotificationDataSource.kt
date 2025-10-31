package com.example.workerapp.data.source

import com.example.workerapp.data.source.model.NotificationItem
import com.example.workerapp.data.source.remote.dto.NetworkResult

interface NotificationDataSource {

    /* *
    * Local
    * */

    /* *
     * Remote
     */
    interface Remote{
        suspend fun getNotifications() : NetworkResult<List<NotificationItem>>

        suspend fun markNotificationAsRead(notificationId: String) : NetworkResult<Unit>
    }
}