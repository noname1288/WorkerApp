package com.example.workerapp.view.calendar

import CalendarWeekPicker
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.workerapp.R
import com.example.workerapp.utils.components.InformationDialog
import java.time.LocalDate

sealed class CalendarScreenSection() {
    object Calendar : CalendarScreenSection()
    object MorningTasks : CalendarScreenSection()
    object AfternoonTasks : CalendarScreenSection()
    object NightTasks : CalendarScreenSection()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(navController: NavController) {
    val sections = listOf(
        CalendarScreenSection.Calendar,
        CalendarScreenSection.MorningTasks,
        CalendarScreenSection.AfternoonTasks,
        CalendarScreenSection.NightTasks
    )

    val today = remember { LocalDate.now() }
    var selectedDay by remember { mutableStateOf<LocalDate?>(today) }
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog){
        InformationDialog (
            onDismissRequest = {
                showDialog = false
            }
        )
    }

    LazyColumn(Modifier.fillMaxSize()) {
        item {
            CenterAlignedTopAppBar(
                title = { Text("Lịch làm việc", fontWeight = FontWeight.Bold) },
                windowInsets = WindowInsets(0, 0, 0, 0),
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
            Spacer(Modifier.height(16.dp))
        }

        sections.forEach { section ->
            when (section) {
                is CalendarScreenSection.Calendar -> {
                    item {
                        Column(
                            Modifier
                                .fillMaxWidth()
                                .background(Color.White)
                                .padding(horizontal = 16.dp)
                        ) {
                            CalendarWeekPicker(today = today, onDateSelected = { selectedDay = it })

                            Spacer(Modifier.height(12.dp))

                            Text("Selected Day: ${selectedDay}")
                        }
                        Spacer(Modifier.height(24.dp))
                    }
                }

                is CalendarScreenSection.MorningTasks -> {
                    item {
                        Text(
                            "Ca sáng",
                            Modifier.padding(horizontal = 16.dp),
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = colorResource(R.color.subtext)
                            )
                        )
                        DaySchedule()
                        Spacer(Modifier.height(12.dp))

                    }
                }

                is CalendarScreenSection.AfternoonTasks -> {
                    item {
                        Text(
                            "Ca chiều",
                            Modifier.padding(horizontal = 16.dp),
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = colorResource(R.color.subtext)
                            )
                        )
                    }
                    items(3) {
                        TaskItem(onClick = {showDialog = true})
                    }
                }

                is CalendarScreenSection.NightTasks -> {
                    item {
                        Text(
                            "Ca tối",
                            Modifier.padding(horizontal = 16.dp),
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = colorResource(R.color.subtext)
                            )
                        )
                        DaySchedule()
                        Spacer(Modifier.height(12.dp))
                    }
                }
            }

        }

    }
}

@Composable
fun DaySchedule() {
    TaskItem()
}

@Composable
fun TaskItem(onClick: () -> Unit = {}) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            "08:00 - 12:00",
            style = MaterialTheme.typography.bodyMedium.copy(colorResource(R.color.subtext))
        )
        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }
        ) {
            Row(
                Modifier
                    .height(100.dp)
                    .padding(16.dp)
            ) {
                VerticalDivider(thickness = 3.dp, color = colorResource(R.color.light_orange))

                Spacer(Modifier.width(8.dp))

                Column {
                    Text(
                        "Công việc 1",
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold)
                    )

                    Spacer(Modifier.height(8.dp))

                    Row(verticalAlignment = Alignment.Top) {
                        Icon(
                            Icons.Filled.LocationOn, null,
                            tint = colorResource(R.color.color_icon),
                            modifier = Modifier
                                .size(16.dp)
                                .padding(top = 4.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            "336 Đ. Nguyễn Trãi, Thanh Xuân Trung, Thanh Xuân, Hà Nội, Việt Nam",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = colorResource(R.color.subtext)
                            )
                        )
                    }
                }
            }
        }

    }
}


@Preview(showBackground = true, backgroundColor = 0xFFDCDCDC)
@Composable
fun PreviewCalendarScreen() {
    CalendarScreen(navController = NavController(LocalContext.current))


}

