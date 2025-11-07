package com.example.workerapp.presentation.screens.notification_chat

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.workerapp.R
import com.example.workerapp.presentation.screens.notification_chat.chat.ChatView
import com.example.workerapp.presentation.screens.notification_chat.notification.NotificationView

enum class NotificationDestinationType {
    Message, Notification
}

data class NotificationTabDestination(
    val label: String,
    val type: NotificationDestinationType
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenRoot(
    modifier: Modifier = Modifier,
    rootViewModel: RootViewModel,
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
                    NotificationView(
                        viewModel = rootViewModel,
                    )
                }

                1 -> {
                    ChatView(navController = navController, viewModel = rootViewModel)
                }
            }
        }
    }
}
