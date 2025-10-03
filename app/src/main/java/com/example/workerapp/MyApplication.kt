package com.example.workerapp

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import com.example.workerapp.data.JobServiceRepository
import com.example.workerapp.data.TokenRepository
import com.example.workerapp.data.UserRepository
import com.example.workerapp.data.repository.JobServiceRepositoryImpl
import com.example.workerapp.data.repository.TokenRepositoryImpl
import com.example.workerapp.data.repository.UserRepositoryImpl
import com.example.workerapp.data.source.local.JobServiceLocalImpl
import com.example.workerapp.data.source.local.TokenLocalImpl
import com.example.workerapp.data.source.local.UserLocalImpl
import com.example.workerapp.data.source.local.room.AppDatabase
import com.example.workerapp.data.source.remote.JobServiceRemoteImpl
import com.example.workerapp.data.source.remote.UserRemoteImpl
import com.example.workerapp.di.dataStore
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        //create notification channel
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        )

        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
    }

    companion object {
        const val CHANNEL_ID = "my_channel_id"
        private const val CHANNEL_NAME = "Worker Application"
    }

}