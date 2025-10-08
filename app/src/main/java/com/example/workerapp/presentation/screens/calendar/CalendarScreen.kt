package com.example.workerapp.presentation.screens.calendar

import CalendarWeekPicker
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.example.workerapp.data.source.model.base.JobModel1
import com.example.workerapp.data.source.model.base.UserModel
import com.example.workerapp.ui.calendar.CalendarUiState
import com.example.workerapp.ui.calendar.CalendarViewModel
import com.example.workerapp.utils.ServiceType
import com.example.workerapp.utils.TimeUtils
import com.example.workerapp.utils.components.CircleLoadingIndicator
import com.example.workerapp.utils.components.InformationDialog
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    modifier: Modifier = Modifier,
    viewModel: CalendarViewModel, navController: NavController
) {
    val context = LocalContext.current
    val tag = "CalendarScreen"

    val today = remember { LocalDate.now() }
    var selectedDay by rememberSaveable { mutableStateOf<LocalDate>(today) }
    var showDialog by remember { mutableStateOf(false) }
    var selectedJob by remember { mutableStateOf(JobModel1()) }

    val taskUiState by viewModel.uiState.collectAsState()

    LaunchedEffect(selectedDay) {
        Log.d(tag, "Selected day changed: $selectedDay")
        val selectedDayString = TimeUtils.toStringWithFormatter(selectedDay)
        viewModel.fetchData(selectedDayString)
    }

    if (showDialog) {
        Log.d("CalendarScreen", "Selected Job: $selectedJob")
        InformationDialog(
            job = selectedJob,
            onDismissRequest = {
                showDialog = false
            }
        )
    }

    LazyColumn(modifier.fillMaxSize()) {
        item {
            CenterAlignedTopAppBar(
                title = { Text("Lịch làm việc", fontWeight = FontWeight.Bold) },
                windowInsets = WindowInsets(0, 0, 0, 0),
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
            Spacer(Modifier.height(16.dp))
        }

        item {
            Box(
                Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                CalendarWeekPicker(
                    today = selectedDay,
                    onDateSelected = { selectedDay = it })
            }

            Spacer(Modifier.height(24.dp))
        }

        item {
            when (taskUiState) {
                is CalendarUiState.Error -> {
                    Toast.makeText(
                        context,
                        (taskUiState as CalendarUiState.Error).message,
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }

                is CalendarUiState.Success -> {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .heightIn(min = 400.dp)
                            .padding(horizontal = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        val jobList = (taskUiState as CalendarUiState.Success).jobs
                        if (jobList.isEmpty()) {
                            Text(
                                "Không có công việc nào trong ngày",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    colorResource(R.color.subtext),
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        } else {
                            jobList.forEach { (timeShift, jobs) ->
                                Text(
                                    timeShift,
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = colorResource(R.color.subtext)
                                    )
                                )

                                Spacer(Modifier.height(4.dp))

                                if (jobs.isEmpty()) {
                                    Text("Không có công việc nào trong ca này")
                                } else {
                                    jobs.forEachIndexed { index, job ->
                                        Log.d("CalendarScreen", "Job $index: $job")
                                        TaskItem(job, onClick = {
                                            selectedJob = job
                                            showDialog = true
                                        })
                                        Spacer(Modifier.height(12.dp))
                                    }
                                }
                            }
                        }
                        Spacer(Modifier.height(12.dp))
                    }
                }

                CalendarUiState.Loading -> {
                    CircleLoadingIndicator()
                }

                is CalendarUiState.Idle -> {}
            }
        }
    }
}

@Composable
fun TaskItem(job: JobModel1, onClick: () -> Unit = {}) {
    val nameOfJob = when (job.serviceType) {
        ServiceType.CleaningType -> "Dọn dẹp"
        ServiceType.HealthcareType -> "Chăm sóc"
        ServiceType.MaintenanceType -> "Bảo trì"
        else -> "Khác"
    }

    Column(Modifier.fillMaxWidth()) {
        Text(
            "${job.startTime}",
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
                    .padding(16.dp)
                    .height(IntrinsicSize.Min)
            ) {
                VerticalDivider(
                    thickness = 3.dp,
                    color = colorResource(R.color.light_orange),
                    modifier = Modifier.fillMaxHeight()
                )

                Spacer(Modifier.width(8.dp))

                Column(Modifier.weight(1f)) {
                    Text(
                        nameOfJob,
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
                            job.location,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = colorResource(R.color.subtext)
                            ),
                            maxLines = 2
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TaskItemPreview() {
    val job = JobModel1(
        uid = "1",
        serviceType = ServiceType.CleaningType,
        price = 150000.0,
        status = "pending",
        listDays = listOf("24/09/2023"),
        createdAt = "20/09/2023",
        startTime = "08:00",
        location = "123 Đường ABC, ",
        user = UserModel(uid = "u1", username = "Nguyễn Văn A")
    )

    TaskItem(job)
}
