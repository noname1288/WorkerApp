package com.example.workerapp.view.detail.components

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.workerapp.utils.button.SlideToConfirmButton

@Composable
fun ActionButtons() {
    SlideToConfirmButton(
        modifier = Modifier.padding(horizontal = 8.dp),
        onConfirmed = {
            Log.d("SlideButton", "Confirmed!")
        }
    )
}