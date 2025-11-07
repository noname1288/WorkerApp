package com.example.workerapp.presentation.screens.notification_chat.notification

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.workerapp.R
import com.example.workerapp.data.source.model.NotificationItemModel
import com.example.workerapp.presentation.screens.notification_chat.NotificationUiState
import com.example.workerapp.presentation.screens.notification_chat.RootViewModel
import com.example.workerapp.utils.components.CircleLoadingIndicator


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationView(
    modifier: Modifier = Modifier,
    viewModel: RootViewModel,
) {
    val context = LocalContext.current

    val uiState by viewModel.notificationUiState.collectAsState()
    val listItems by viewModel.listNoti.collectAsState()

    var showDialog by remember { mutableStateOf(false) }
    var temp by remember { mutableStateOf<NotificationItemModel?>(null) }

    LaunchedEffect(Unit) {
        viewModel.fetchAllNotifications()
    }

    Column(
        Modifier
            .fillMaxWidth()
            .heightIn(min = 300.dp)
    ) {
        when (uiState) {
            is NotificationUiState.Error -> {
                LaunchedEffect(uiState) {
                    Toast.makeText(
                        context,
                        (uiState as NotificationUiState.Error).message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            NotificationUiState.Idle -> {}

            NotificationUiState.Loading -> {
                Box(
                    Modifier
                        .fillMaxSize()
                        .background(Color.LightGray.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircleLoadingIndicator()
                }
            }

            is NotificationUiState.Success -> {
                if (listItems.isEmpty()) {
                    Text("Không có thông báo nào")
                } else {
                    listItems.forEach { item ->
                        NotificationItemRow(item, onClick = {
                            if (!item.isRead) {
                                viewModel.markAsRead(item.uid)
                            }
                            showDialog = true
                            temp = item
                        })
                        HorizontalDivider()
                    }
                }
            }
        }

        if (showDialog && temp != null) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                confirmButton = {
                    Text(
                        text = "Đóng",
                        modifier = Modifier
                            .padding(8.dp)
                            .clickable { showDialog = false },
                        color = colorResource(R.color.orange_primary),
                        fontWeight = FontWeight.Bold
                    )
                },
                title = {
                    Text(
                        text = temp!!.title,
                        fontWeight = FontWeight.Bold
                    )
                },
                text = {
                    Text(
                        text = temp!!.content,
                    )
                }
            )
        }
    }
}

@Composable
fun NotificationItemRow(item: NotificationItemModel = NotificationItemModel(), onClick: () -> Unit) {
    val hasRead = item.isRead

    Row(
        Modifier
            .fillMaxWidth()
            .background(Color.White)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            Modifier
                .weight(1f)
                .padding(horizontal = 16.dp, vertical = 8.dp),
        ) {
            Text(
                item.title,
                maxLines = 2,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(Modifier.height(6.dp))

            Text(
                item.content,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = if (hasRead) FontWeight.Normal else FontWeight.Bold
                ),
                maxLines = 1
            )

            Spacer(Modifier.height(4.dp))

            Text(
                item.createdAt,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = colorResource(R.color.subtext),
                    fontWeight = if (hasRead) FontWeight.Normal else FontWeight.Bold
                )
            )
        }

        if (!hasRead)
            Box {
                Icon(
                    Icons.Default.Circle, null, tint = colorResource(R.color.light_orange_icon),
                    modifier = Modifier.size(8.dp).padding(end = 8.dp)
                )
            }
    }
}