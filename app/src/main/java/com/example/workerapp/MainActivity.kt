package com.example.workerapp

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.workerapp.data.source.local.AppCache
import com.example.workerapp.navigation.AppRoutes
import com.example.workerapp.presentation.screens.notification_chat.RequestNotificationPermission
import com.example.workerapp.service.NotificationService
import com.example.workerapp.ui.base.BaseScreen
import com.example.workerapp.ui.theme.WorkerAppTheme
import com.example.workerapp.utils.ManifestUtils
import com.example.workerapp.utils.ext.navigateWithArgs
import com.google.android.libraries.places.api.Places
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableSharedFlow
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var appCache : AppCache

    // Phễu nhận intents (replay=1 để nhận cả intent khởi động)
    private val intentFlow = MutableSharedFlow<Intent>(replay = 1, extraBufferCapacity = 1)

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Đẩy intent đầu tiên (cold start)
        intentFlow.tryEmit(intent)

        val apiKey = ManifestUtils.getApiKeyFromManifest(this)

        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, apiKey)
        }

        setContent {
            val navController = rememberNavController()

            // Xin quyền thông báo nếu cần
            val isGrantedForNotification = appCache.getNotificationPermission()
                .collectAsState(false)

            if (!isGrantedForNotification.value) {
                RequestNotificationPermission()
            }


            WorkerAppTheme {
                // LẮNG NGHE INTENT VÀ ĐIỀU HƯỚNG
                LaunchedEffect(Unit) {
                    intentFlow.collect { latest ->
                        handleDeepLink(navController, latest)
                    }
                }
                BaseScreen(navController) // truyền navController vào BaseScreen
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        Log.d("MainActivity", "onNewIntent called $intent")
        // Đẩy intent mới vào flow -> Compose sẽ collect và navigate
        intentFlow.tryEmit(intent)
    }

    private fun handleDeepLink(navController: NavController, intent: Intent) {
        val route = intent.getStringExtra(NotificationService.ROUTE_TO_CHAT_DETAIL)
        val roomId = intent.getStringExtra(NotificationService.CHAT_ROOM_ID) ?: return
        val partnerName = intent.getStringExtra(NotificationService.PARTNER_NAME) ?: return
        val partnerAvatar = intent.getStringExtra(NotificationService.PARTNER_AVATAR) ?: return

        val encodedName = URLEncoder.encode(
            partnerName,
            StandardCharsets.UTF_8.toString()
        )

        val encodedAvatar = URLEncoder.encode(
            partnerAvatar,
            StandardCharsets.UTF_8.toString()
        )

        if (route == AppRoutes.CHAT_DETAIL && !roomId.isNullOrBlank()) {
            navController.navigateWithArgs(AppRoutes.CHAT_DETAIL, args = arrayOf(roomId, encodedName, encodedAvatar))
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d("MainActivity", "onStart called")
    }

    override fun onStop() {
        super.onStop()
        Log.d("MainActivity", "onStop called")
        Log.d("LifecycleCheck", "onStop() — PID: ${android.os.Process.myPid()}")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("MainActivity", "onDestroy called")
    }
}
