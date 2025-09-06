package com.example.workerapp.view.detail.cleaning

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.workerapp.R
import com.example.workerapp.data.model.CleaningJobModel
import com.example.workerapp.data.model.CleaningServiceModel
import com.example.workerapp.data.model.base.JobModel
import com.example.workerapp.data.model.base.UserModel
import com.example.workerapp.view.detail.components.ActionButtons
import com.example.workerapp.view.detail.components.JobDetailCard
import com.example.workerapp.view.detail.components.JobServiceBottomSheet
import com.example.workerapp.view.detail.components.WeeklySchedule
import com.example.workerapp.view.detail.components.JobWorkflow
import com.example.workerapp.view.detail.components.ClientCard

sealed class CleaningJobSection {
    data class UserInfo(val user: UserModel) : CleaningJobSection()
    data class JobDetails(val job: CleaningJobModel) : CleaningJobSection()
    data class WeeklySchedule(val days: List<Int>, val isWeekly: Boolean) : CleaningJobSection()
    data class AdditionalJob(val isCooking: Boolean, val isIroning: Boolean) : CleaningJobSection()
    object JobWorkflow : CleaningJobSection()
    object ActionButtons : CleaningJobSection()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CleaningDetailScreen() {
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
    val fakeRoomCleaningServices = listOf(
        CleaningServiceModel(
            id = "kitchen_cleaning",
            duties = listOf("Wipe countertops", "Clean sink", "Mop floor", "Clean appliances"),
            imageUrl = "https://example.com/kitchen.jpg",
            serviceType = "Cleaning",
            serviceName = "Kitchen Cleaning"
        ),
        CleaningServiceModel(
            id = "living_room_cleaning",
            duties = listOf("Vacuum carpet", "Dust furniture", "Clean windows", "Organize shelves"),
            imageUrl = "https://example.com/living_room.jpg",
            serviceType = "Cleaning",
            serviceName = "Living Room Cleaning"
        ),
        CleaningServiceModel(
            id = "bathroom_cleaning",
            duties = listOf("Scrub toilet", "Clean shower", "Wipe mirrors", "Mop floor"),
            imageUrl = "https://example.com/bathroom.jpg",
            serviceType = "Cleaning",
            serviceName = "Bath Room Cleaning"
        )
    )


    var isShowBottomSheet by remember { mutableStateOf(false) }

    val sections = listOf(
        CleaningJobSection.UserInfo(fakeUser),
        CleaningJobSection.JobDetails(fakeCleaningJob),
        CleaningJobSection.WeeklySchedule(fakeDays, fakeCleaningJob.jobDetail.isWeek),
        CleaningJobSection.AdditionalJob(fakeCleaningJob.isCooking, fakeCleaningJob.isIroning),
        CleaningJobSection.JobWorkflow,
        CleaningJobSection.ActionButtons
    )

    Column(Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Chi tiết công việc") },
            navigationIcon = {
                IconButton(onClick = {}) {
                    Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = colorResource(R.color.light_orange)
            )
        )
        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            item {
                Spacer(Modifier.height(16.dp))
            }
            sections.forEach { section ->
                when (section) {
                    is CleaningJobSection.UserInfo -> {
                        item {
                            ClientCard(user = section.user)
                            Spacer(Modifier.height(24.dp))
                        }
                    }

                    is CleaningJobSection.JobDetails -> {
                        item {
                            JobDetailCard(cleaningJob = section.job)
                            Spacer(Modifier.height(24.dp))
                        }
                    }

                    is CleaningJobSection.WeeklySchedule -> {
                        item {
                            WeeklySchedule(section.days)
                            Spacer(Modifier.height(48.dp))
                        }
                    }

                    is CleaningJobSection.AdditionalJob -> {
                        item {
                            CustomAdditionalJob(section.isCooking, section.isIroning)
                            Spacer(Modifier.height(64.dp))

                        }
                    }

                    is CleaningJobSection.JobWorkflow -> {
                        item {
                            JobWorkflow(onClick = { isShowBottomSheet = true })
                            Spacer(Modifier.height(24.dp))
                        }
                    }

                    is CleaningJobSection.ActionButtons -> {
                        item {
                            ActionButtons()
                            Spacer(Modifier.height(32.dp))
                        }
                    }
                }
            }

            item {
                if (isShowBottomSheet) {
                    JobServiceBottomSheet(
                        items = fakeRoomCleaningServices,
                        onDismiss = { isShowBottomSheet = false }
                    )
                }
            }
        }
    }

}

@Composable
fun CustomAdditionalJob(isCooking: Boolean = true, isIroning: Boolean = true) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
    ) {
        if (isCooking) {
            Box(
                Modifier
                    .size(108.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(colorResource(R.color.light_gray)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painterResource(R.drawable.ic_cooking), null,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(Modifier.height(12.dp))
                    Text("Nấu ăn: 1 giờ", fontSize = 12.sp)
                }
            }
        }

        if (isIroning) {
            Box(
                Modifier
                    .size(108.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(colorResource(R.color.light_gray)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painterResource(R.drawable.ic_iron), null,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(Modifier.height(12.dp))
                    Text("Ủi đồ: 1 giờ", fontSize = 12.sp)
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PrevJobDetail1(modifier: Modifier = Modifier) {
    UserModel(
        username = "Phạm Thanh Sơn",
        gender = "Male",
        dob = "1990-01-01",
        avatar = "https://example.com/avatar.jpg",
        tel = "1234567890",
        location = "New York",
        email = "john.doe@example.com",
        role = "user"
    )
    CleaningJobModel(
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
            isWeek = false,
            dayOfWeek = 3,
            createdAt = 1622516400000L,
            status = "Pending"
        )
    )
}
