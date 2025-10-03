package com.example.workerapp.service

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.workerapp.MyApplication
import com.example.workerapp.R
import com.example.workerapp.data.TokenRepository
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class NotificationService : FirebaseMessagingService() {
    @Inject
    lateinit var tokenRepository: TokenRepository

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.d(TAG, "From: ${message.from}")
        val notificationItem = message.notification
        if (notificationItem != null) {
            showNotification(notificationItem.title ?: "My App", notificationItem.body ?: "My body")
        }

    }

    private fun showNotification(title: String, message: String) {
        val builder = NotificationCompat.Builder(this, MyApplication.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        // Lấy NotificationManagerCompat
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED){
            with(NotificationManagerCompat.from(this)){
                notify(System.currentTimeMillis().toInt(), builder.build())
            }
        }else {
            Log.w(TAG, "Permission to post notifications not granted.")
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "FCM Token: $token")

        //save to local data store
        CoroutineScope(Dispatchers.IO).launch {
            tokenRepository.saveFcmToken(token)
        }
    }

    companion object {
        private const val TAG = "NotificationService"
    }
}