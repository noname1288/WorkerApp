package com.example.workerapp.data.repository

import com.example.workerapp.data.NotificationRepository
import com.example.workerapp.data.source.NotificationDataSource
import com.example.workerapp.data.source.model.NotificationItem
import com.example.workerapp.data.source.remote.NotificationRemoteImpl
import com.example.workerapp.data.source.remote.dto.NetworkResult

class NotificationRepositoryImpl(private val remote: NotificationDataSource.Remote) :
    NotificationRepository {
    override suspend fun getNotifications(): Result<List<NotificationItem>> {
        return try {
            when (val response = remote.getNotifications()) {
                is NetworkResult.Success -> {
                    Result.success(response.data)
                }

                is NetworkResult.Error -> {
                    Result.failure(Exception(response.message))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    companion object {
        private const val TAG = "NotificationRepositoryImpl"

        private var instance: NotificationRepositoryImpl? = null

        fun getInstance(): NotificationRepositoryImpl {
            if (instance == null) {
                instance = NotificationRepositoryImpl(remote = NotificationRemoteImpl.getInstance())
            }
            return instance!!
        }
    }
}
