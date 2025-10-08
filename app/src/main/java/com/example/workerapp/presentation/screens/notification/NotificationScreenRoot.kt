package com.example.workerapp.presentation.screens.notification

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.workerapp.R
import com.example.workerapp.data.source.model.NotificationItem
import com.example.workerapp.navigation.AppRoutes
import com.example.workerapp.utils.ServiceType
import com.example.workerapp.utils.components.CircleLoadingIndicator
import com.example.workerapp.utils.ext.navigateWithArgs

enum class NotificationDestinationType {
    Message, Notification
}

data class NotificationTabDestination(
    val label: String,
    val type: NotificationDestinationType
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreenRoot(
    modifier: Modifier = Modifier,
    notificationViewModel: NotificationViewModel,
    navController: NavController
) {
    val destinations = listOf(
        NotificationTabDestination("Hệ thống", NotificationDestinationType.Notification),
        NotificationTabDestination("Tin nhắn", NotificationDestinationType.Message),
    )

    var selectedDestination by rememberSaveable { mutableIntStateOf(0) }

    LazyColumn(modifier.fillMaxSize()) {
        item {
            //Header
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        stringResource(R.string.notification_title),
                        fontWeight = FontWeight.Bold
                    )
                },
                windowInsets = WindowInsets(0, 0, 0, 0),
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
            HorizontalDivider()
        }
        item {
            //Tabs
            SecondaryTabRow(
                selectedTabIndex = selectedDestination,
                containerColor = Color.White,
                indicator = {
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(
                            selectedDestination,
                            matchContentSize = false
                        ),
                        color = colorResource(R.color.orange_primary),
                    )
                }
            ) {
                destinations.forEachIndexed { index, destination ->
                    val isSelected = index == selectedDestination
                    Tab(
                        selected = isSelected,
                        onClick = { selectedDestination = index },
                        text = {
                            Text(
                                destination.label,
                                maxLines = 1,
                                color = if (isSelected) Color.Black else colorResource(R.color.subtext)
                            )
                        })
                }
            }
            Spacer(Modifier.height(12.dp))
        }

        item {
            when (selectedDestination) {
                0 -> {
                    NotificationScreen(
                        viewModel = notificationViewModel,
                        navController = navController
                    )
                }

                1 -> {
                    Text("Chat Screen")
                }
            }
        }
    }
}

@Composable
fun NotificationScreen(
    modifier: Modifier = Modifier,
    viewModel: NotificationViewModel,
    navController: NavController
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    val listItems by viewModel.listItems.collectAsState()

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
                CircleLoadingIndicator()
            }

            is NotificationUiState.Success -> {
                if (listItems.isEmpty()) {
                    Text("Không có thông báo nào")
                } else {
                    listItems.forEach { item ->
                        NotificationItem(item, onClick = {})
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}

@Composable
fun NotificationItem(item: NotificationItem = NotificationItem(), onClick: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .background(Color.White)
            .clickable { onClick() }
    ) {
        Column(
            Modifier
                .weight(1f)
                .padding(horizontal = 16.dp, vertical = 8.dp),
        ) {
            Text(
                item.createdAt,
                style = MaterialTheme.typography.bodySmall.copy(color = colorResource(R.color.subtext))
            )

            Spacer(Modifier.height(4.dp))

            Text(
                item.title,
                maxLines = 2,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(Modifier.height(4.dp))

            Text(
                item.content,
                style = MaterialTheme.typography.bodyMedium.copy(color = colorResource(R.color.subtext)),
                maxLines = 1
            )
        }

        if (!item.isRead)
            Box(Modifier.padding(16.dp), contentAlignment = Alignment.Center) {
                Icon(
                    Icons.Default.Circle,
                    contentDescription = "isRead",
                    modifier = Modifier.size(8.dp),
                    tint = Color.Red
                )
            }
    }
}
