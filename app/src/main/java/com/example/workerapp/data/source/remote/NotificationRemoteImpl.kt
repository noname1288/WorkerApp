package com.example.workerapp.data.source.remote

import android.util.Log
import com.example.workerapp.data.source.model.NotificationItem
import com.example.workerapp.data.source.NotificationDataSource
import com.example.workerapp.data.source.remote.api.NotificationApi
import com.example.workerapp.data.source.remote.dto.NetworkResult
import com.example.workerapp.data.source.remote.dto.response.ApiErrorResponse
import com.squareup.moshi.Moshi
import javax.inject.Inject

class NotificationRemoteImpl @Inject constructor(
    private val notificationApi: NotificationApi,
    moshi: Moshi
) : NotificationDataSource.Remote {
    private val errorAdapter = moshi.adapter(ApiErrorResponse::class.java)

    override suspend fun getNotifications(): NetworkResult<List<NotificationItem>> {
        val response = notificationApi.getAllNotifications()

        return if (response.isSuccessful){
            val body = response.body()

            if (body != null && body.success){
                return NetworkResult.Success(body.notifications)
            } else {
                val message = body?.message ?: "Unknown error occurred"
                Log.e(TAG, "getNotifications: API returned unsuccessful response: $message" )
                return NetworkResult.Error(message)
            }
        }else {
            val errorMessage = response.errorBody()?.string()
                ?.let { json -> errorAdapter.fromJson(json)?.error }
                ?: response.message()
                ?: "Request failed with status code ${response.code()}"

            Log.e(UserRemoteImpl.Companion.TAG, "Fetch All Notifications Exception: $errorMessage")
            NetworkResult.Error(errorMessage)
        }
    }

    companion object {
        private const val TAG = "NotificationRemoteImpl"
    }
}
