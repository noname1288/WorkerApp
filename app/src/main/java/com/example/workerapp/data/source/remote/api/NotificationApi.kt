package com.example.workerapp.data.source.remote.api

import com.example.workerapp.data.source.remote.dto.response.NotificationsResponse
import com.example.workerapp.utils.annotation.AuthRequired
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface NotificationApi {

    @AuthRequired
    @GET("notifications")
    suspend fun getAllNotifications(): Response<NotificationsResponse>

    @AuthRequired
    @PUT("notifications/{notificationId}")
    suspend fun markNotificationAsRead(
        @Path("notificationId") notificationId: String
    ): Response<NotificationsResponse>

}
