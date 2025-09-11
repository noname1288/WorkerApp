package com.example.workerapp.ui.detail.healcare

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.workerapp.R
import com.example.workerapp.data.model.CleaningJobModel
import com.example.workerapp.data.model.base.JobModel
import com.example.workerapp.data.model.base.UserModel
import com.example.workerapp.utils.button.SlideToConfirmButton
import com.example.workerapp.ui.detail.components.WeeklySchedule
import com.example.workerapp.ui.detail.components.JobWorkflow
import com.example.workerapp.ui.detail.components.ClientCard
import com.example.workerapp.ui.detail.components.JobDetailCard

sealed class HealthcareJobSection {
    data class UserInfo(val user: UserModel) : HealthcareJobSection()
    object JobDetails : HealthcareJobSection()
    data class WeeklySchedule(val days: List<Int>, val isWeekly: Boolean) : HealthcareJobSection()
    object JobWorkflow : HealthcareJobSection()
    object ActionButtons : HealthcareJobSection()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HealthcareDetailScreen(modifier: Modifier = Modifier) {
    val TAG = "HealthcareDetailScreen"

    val fakeUser = UserModel(
        username = "Phạm Thanh Sơn",
        gender = "Male",
        dob = "1990-01-01",
        avatar = "https://example.com/avatar.jpg",
        tel = "1234567890",
        location = "New York",
        email = "john.doe@example.com",
        role = "user"
    )
    val fakeCleaningJob = CleaningJobModel(
        id = "job123",
        durationID = "duration456",
        services = listOf("Floor Cleaning", "Window Washing"),
        isCooking = true,
        isIroning = true,
        jobDetail = JobModel(
            serviceType = "Cleaning",
            startTime = 1622520000000L,
            endTime = 1622523600000L,
            workerQuantity = 2,
            price = 1500000.0,
            isWeek = true,
            dayOfWeek = 3,
            createdAt = 1622516400000L,
            status = "Pending"
        )
    )

    val fakeDays = listOf(2, 3, 4)

    var isShowBottomSheet by remember { mutableStateOf(false) }

    val sections = listOf(
        HealthcareJobSection.UserInfo(fakeUser),
        HealthcareJobSection.JobDetails,
        HealthcareJobSection.WeeklySchedule(fakeDays, isWeekly = true),
        HealthcareJobSection.JobWorkflow,
        HealthcareJobSection.ActionButtons
    )

    Column {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    stringResource(R.string.job_detail_title),
                    fontWeight = FontWeight.Bold
                )
            },
            windowInsets = WindowInsets(0, 0, 0, 0),
            navigationIcon = {
                IconButton(onClick = {}) {
                    Icon(
                        Icons.Default.ArrowBackIosNew, contentDescription = "Back",
                        modifier = Modifier.size(20.dp)
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
        )
        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            item{
                Spacer(Modifier.height(24.dp))
            }

            sections.forEach { section ->
                when(section){
                    is HealthcareJobSection.UserInfo -> {
                        item{
                            ClientCard(user = section.user)
                            Spacer(Modifier.height(12.dp))
                        }
                    }
                    is HealthcareJobSection.JobDetails ->{
                        item{
                            JobDetailCard(fakeCleaningJob)
                            Spacer(Modifier.height(12.dp))
                        }
                    }
                    is HealthcareJobSection.WeeklySchedule -> {
                        item {
                            WeeklySchedule(section.days)
                            Spacer(Modifier.height(24.dp))
                        }
                    }
                    is HealthcareJobSection.JobWorkflow -> {
                        item {
                            JobWorkflow(onClick = { isShowBottomSheet = true })
                            Spacer(Modifier.height(24.dp))
                        }
                    }
                    is HealthcareJobSection.ActionButtons -> {
                        item {
                            SlideToConfirmButton (
                                onConfirmed = {
                                    Log.d(TAG, "HealthDetailScreen: Confirmed")

                                }
                            )
                            Spacer(Modifier.height(24.dp))
                        }
                    }
                    else -> {}
                }
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun HealthcareDetailScreenPreview() {
    HealthcareDetailScreen()
}