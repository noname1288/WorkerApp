package com.example.workerapp.data.source.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.workerapp.data.source.model.NotificationItemModel

@Dao
interface NotificationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotifications(notifications: List<NotificationItemModel>)

    @Query("SELECT * FROM notifications")
    suspend fun getAllNotifications(): List<NotificationItemModel>
}
