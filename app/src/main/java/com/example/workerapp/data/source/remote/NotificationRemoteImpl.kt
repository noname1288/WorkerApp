package com.example.workerapp.data.source.remote

import android.util.Log
import com.example.workerapp.data.source.model.NotificationItem
import com.example.workerapp.data.source.NotificationDataSource
import com.example.workerapp.data.source.remote.api.NotificationApi
import com.example.workerapp.data.source.remote.dto.NetworkResult

class NotificationRemoteImpl(private val notificationApi: NotificationApi) : NotificationDataSource.Remote {
    override suspend fun getNotifications(): NetworkResult<List<NotificationItem>> {
        try {
            val response = notificationApi.getAllNotifications()

            return if (response.success) {
                Log.d(TAG, "getNotifications: ${response.notifications}")
                NetworkResult.Success(response.notifications)
            } else {
                Log.e(TAG, "getNotifications error: ${response.message}")
                NetworkResult.Error(response.message)
            }
        }catch (e: Exception){
            Log.e(TAG, "getNotifications Exception: ${e.message}")
            return NetworkResult.Error(e.message ?: "An unknown error occurred")
        }
    }

    companion object{
        private const val TAG = "NotificationRemoteImpl"

        private var instance: NotificationRemoteImpl? = null
        fun getInstance(): NotificationRemoteImpl {
            if (instance == null) {
                instance = NotificationRemoteImpl(RetrofitHelper.notificationApi)
            }
            return instance!!
        }
    }
}
