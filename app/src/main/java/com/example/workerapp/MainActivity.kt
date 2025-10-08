package com.example.workerapp

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.lifecycleScope
import com.example.workerapp.data.source.local.AppCache
import com.example.workerapp.presentation.screens.notification.RequestNotificationPermission
import com.example.workerapp.ui.base.BaseScreen
import com.example.workerapp.ui.theme.WorkerAppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var appCache : AppCache

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        Log.d("MainActivity", "onCreate called")
        Log.d("LifecycleCheck", "onCreate() — PID: ${android.os.Process.myPid()}")

        setContent {
            var isGrantedForNotification = appCache.getNotifiticationPermission().collectAsState(false)

            if (!isGrantedForNotification.value){
                RequestNotificationPermission()
            }

            WorkerAppTheme {
                BaseScreen()
            }
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
