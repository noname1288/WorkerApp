package com.example.workerapp.service

import android.Manifest
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.workerapp.MainActivity
import com.example.workerapp.MyApplication
import com.example.workerapp.R
import com.example.workerapp.data.TokenRepository
import com.example.workerapp.navigation.AppRoutes
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
        Log.d(TAG, "Message data payload: ${message.data}")

        val metaData = message.data
        val type = metaData[NOTIFICATION_TYPE]
        val roomId = metaData[CHAT_ROOM_ID]
        val partnerName = metaData[PARTNER_NAME]
        val partnerAvatar = metaData[PARTNER_AVATAR]

        val title = metaData["senderName"] ?: "New Message"
        val body = metaData["content"] ?: "You have received a new message."

        if (type == "new_message") {

            if (MyApplication.isForeground){
                //send broadcast to update chat UI
            }else {
                showMessageNotification(
                    title,
                    body,
                    roomId = roomId,
                    partnerName = partnerName,
                    partnerAvatar = partnerAvatar
                )
            }
        }
    }

    private fun showMessageNotification(
        title: String?,
        message: String?,
        roomId: String?,
        partnerName: String?,
        partnerAvatar: String?
    ) {
        val clickIntent = Intent(this, MainActivity::class.java).apply {
            // Để hệ thống đưa instance hiện có ra trước và đẩy intent vào onNewIntent
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            putExtra(ROUTE_TO_CHAT_DETAIL, AppRoutes.CHAT_DETAIL)
            putExtra(CHAT_ROOM_ID, roomId)
            putExtra(PARTNER_NAME, partnerName)
            putExtra(PARTNER_AVATAR, partnerAvatar)
        }

        val pendingIntent = TaskStackBuilder.create(this).run {
            addNextIntentWithParentStack(clickIntent)
            getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        }

        val notificationBuilder = NotificationCompat.Builder(this, MyApplication.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle(title ?: "New Message")
            .setContentText(message ?: "You have received a new message.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            with(NotificationManagerCompat.from(this)) {
                notify(System.currentTimeMillis().toInt(), notificationBuilder.build())
            }
        } else {
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
        const val NOTIFICATION_TYPE = "type"
        const val CHAT_ROOM_ID = "chat_room_id"
        const val PARTNER_NAME = "partner_name"
        const val PARTNER_AVATAR = "partner_avatar"
        const val ROUTE_TO_CHAT_DETAIL = "route_to_chat_detail"

        private const val TAG = "NotificationService"
    }
}
