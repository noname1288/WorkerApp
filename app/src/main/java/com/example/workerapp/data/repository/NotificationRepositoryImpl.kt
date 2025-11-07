package com.example.workerapp.data.repository

import com.example.workerapp.data.NotificationRepository
import com.example.workerapp.data.source.NotificationDataSource
import com.example.workerapp.data.source.model.NotificationItemModel
import com.example.workerapp.data.source.remote.dto.NetworkResult
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val local: NotificationDataSource.Local,
    private val remote: NotificationDataSource.Remote
) : NotificationRepository {
    override suspend fun getNotifications(): Result<List<NotificationItemModel>> {
        return try {
            val response = remote.getNotifications()
            when (response) {
                is NetworkResult.Success -> {
                    val notifications = response.data

                    //Save to local
                    local.saveNotifications(notifications)

                    //Read from local
                    val cached = local.getNotifications()
                    Result.success(cached)
                }

                is NetworkResult.Error -> {
                    val cached = local.getNotifications()
                    if (cached.isNotEmpty()) {
                        Result.success(cached)
                    } else {
                        Result.failure(Exception(response.message))
                    }
                }
            }
        } catch (e: Exception) {
            val cached = local.getNotifications()
            if (!cached.isNullOrEmpty()) {
                Result.success(cached)
            } else {
                Result.failure(e)
            }
        }
    }

    override suspend fun markNotificationAsRead(notificationId: String): Result<Unit> {
        try {
            val response = remote.markNotificationAsRead(notificationId)

            return when (response) {
                is NetworkResult.Success -> {
                    Result.success(Unit)
                }

                is NetworkResult.Error -> {
                    Result.failure(Exception(response.message))
                }
            }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}
