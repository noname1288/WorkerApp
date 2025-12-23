package com.example.workerapp.data.source.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.workerapp.data.source.local.room.entity.ApplicationModel
import com.example.workerapp.data.source.local.room.entity.UserLocalEntity
import com.example.workerapp.data.source.model.NotificationItemModel
import com.example.workerapp.data.source.model.cleaning.CleaningServiceModel
import com.example.workerapp.data.source.model.healthcare.HealthcareServiceModel
import com.example.workerapp.data.source.model.maintenance.MaintenanceServiceModel
import com.example.workerapp.data.source.model.maintenance.PowerModel

@Database(
    entities = [UserLocalEntity::class, CleaningServiceModel::class, HealthcareServiceModel::class,
        MaintenanceServiceModel::class, PowerModel::class, NotificationItemModel::class, ApplicationModel::class],
    version = 3,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun serviceDao(): ServiceDao
    abstract fun notificationDao(): NotificationDao
    abstract fun applicationDao(): ApplicationDao
}
