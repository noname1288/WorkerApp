package com.example.workerapp.data.repository

import com.example.workerapp.data.NotificationRepository
import com.example.workerapp.data.source.NotificationDataSource
import com.example.workerapp.data.source.model.NotificationItem
import com.example.workerapp.data.source.remote.NotificationRemoteImpl
import com.example.workerapp.data.source.remote.dto.NetworkResult
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val remote: NotificationDataSource.Remote
) : NotificationRepository {
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
}
