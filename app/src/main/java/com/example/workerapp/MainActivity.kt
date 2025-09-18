package com.example.workerapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.workerapp.ui.base.BaseScreen
import com.example.workerapp.ui.theme.WorkerAppTheme


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
