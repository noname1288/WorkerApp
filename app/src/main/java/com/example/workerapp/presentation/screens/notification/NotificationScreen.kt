package com.example.workerapp.presentation.screens.notification

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.workerapp.R

enum class NotificationDestinationType {
    Message, Notification
}

data class NotificationTabDestination(
    val label: String,
    val type: NotificationDestinationType
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(
    modifier: Modifier = Modifier,
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

        items(10) {
            NotificationItem()
            HorizontalDivider()

        }
    }
}

@Composable
fun NotificationItem() {
    Column(
        Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 8.dp),
    ) {
        Text(
            "14:15 | 20/10/2024",
            style = MaterialTheme.typography.bodySmall.copy(color = colorResource(R.color.subtext))
        )

        Spacer(Modifier.height(4.dp))

        Text(
            "ĐÃ ĐẾN GIỜ LÀM VIỆC #1 - NÂNG TẦM DỊCH VỤ CHO BẢN THÂN VÀ GIA ĐÌNH HÔM NAY",
            maxLines = 2,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Bold
            )
        )

        Spacer(Modifier.height(4.dp))

        Text(
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
            style = MaterialTheme.typography.bodyMedium.copy(color = colorResource(R.color.subtext)),
            maxLines = 1
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xF8A66E)
@Composable
fun PrevNotificationScreen() {
    NotificationScreen()
}