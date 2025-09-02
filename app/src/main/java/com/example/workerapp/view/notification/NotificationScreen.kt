package com.example.workerapp.view.notification

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.workerapp.R

@Composable
fun NotificationScreen(modifier: Modifier = Modifier) {
    LazyColumn (modifier.fillMaxSize()){
        item {
            Text(stringResource(R.string.notification_title),
                 fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth())
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PrevNotificationScreen(modifier: Modifier = Modifier) {
    NotificationScreen()
}