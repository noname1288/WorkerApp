package com.example.workerapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import com.example.workerapp.di.dataStore
import com.example.workerapp.ui.base.BaseScreen
import com.example.workerapp.ui.theme.WorkerAppTheme
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            WorkerAppTheme {
                BaseScreen()
            }
        }
    }
}
