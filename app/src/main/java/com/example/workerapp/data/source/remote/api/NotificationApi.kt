package com.example.workerapp.data.source.remote.api

import com.example.workerapp.data.source.model.NotificationItem
import com.example.workerapp.data.source.remote.dto.response.NotificationsResponse
import com.example.workerapp.utils.annotation.AuthRequired
import retrofit2.http.GET

interface NotificationApi {

    @AuthRequired
    @GET("notifications")
    suspend fun getAllNotifications(): NotificationsResponse

}
