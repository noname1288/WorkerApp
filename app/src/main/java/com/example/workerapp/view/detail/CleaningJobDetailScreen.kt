package com.example.workerapp.view.detail

import android.annotation.SuppressLint
import android.util.Log
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.workerapp.R
import com.example.workerapp.data.model.CleaningJobModel
import com.example.workerapp.data.model.base.JobModel
import com.example.workerapp.data.model.base.UserModel
import com.example.workerapp.utils.TimeUtils
import com.example.workerapp.utils.button.CustomSwitch
import com.example.workerapp.utils.button.SlideToConfirmButton

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
fun CleaningJobDetailScreen(modifier: Modifier = Modifier) {
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
                            UserInfoCard(user = section.user)
                            Spacer(Modifier.height(24.dp))
                        }
                    }

                    is CleaningJobSection.JobDetails -> {
                        item {
                            CustomJobDetailsColumn(cleaningJob = section.job)
                            Spacer(Modifier.height(24.dp))
                        }
                    }

                    is CleaningJobSection.WeeklySchedule -> {
                        item {
                            CustomWeeklySchedule(section.days)
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
                            JobWorkflowItem()
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

@Composable
fun CustomWeeklySchedule(selectedDays: List<Int> = emptyList(), isWeekly: Boolean = true) {

    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(stringResource(R.string.weekly_title), fontSize = 16.sp, fontWeight = FontWeight.W500)
        CustomSwitch(
            checked = isWeekly,
            onCheckedChange = { })
    }

    Spacer(Modifier.height(16.dp))

    val days = listOf<String>("T2", "T3", "T4", "T5", "T6", "T7", "CN")
    Row(
        Modifier
            .padding(horizontal = 8.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        days.forEachIndexed { index, day ->
            val isSelected = selectedDays.contains(index)
            DayItem(day, isSelected)
        }
    }
}

@Composable
fun DayItem(label: String, isSelected: Boolean) {
    val backgroundColor =
        if (isSelected) colorResource(R.color.orange) else colorResource(R.color.light_gray)
    val textColor = if (isSelected) Color.White else Color.Black

    Box(
        Modifier
            .size(32.dp)
            .clip(CircleShape)
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Text(label, color = textColor, fontSize = 12.sp)
    }
}

@Composable
fun ActionButtons() {
    SlideToConfirmButton(
        modifier = Modifier.padding(horizontal = 8.dp),
        onConfirmed = {
            Log.d("SlideButton", "Confirmed!")
        }
    )
}

@Composable
fun JobWorkflowItem() {
    Row(
        Modifier
            .height(48.dp)
            .padding(horizontal = 8.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(colorResource(R.color.light_gray))
            .padding(vertical = 12.dp, horizontal = 24.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text("Quy trình công việc", fontSize = 14.sp)
        Icon(Icons.Default.ArrowForwardIos, null, modifier = Modifier.size(20.dp))
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun CustomJobDetailsColumn(cleaningJob: CleaningJobModel) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {
        Text(
            stringResource(R.string.job_detail_title_2),
            fontSize = 16.sp,
            fontWeight = FontWeight.W500
        )

        Spacer(Modifier.height(2.dp))

        Row(
            Modifier.padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.ic_black_circle_24),
                null,
                modifier = Modifier.size(6.dp)
            )
            Text(buildAnnotatedString {
                withStyle(style = SpanStyle(fontSize = 12.sp)) {
                    append("Danh mục: ")
                }
                withStyle(style = SpanStyle(color = colorResource(R.color.orange))) {
                    append(cleaningJob.jobDetail.serviceType)
                }
            })
        }

        Spacer(Modifier.height(2.dp))

        Row(
            Modifier.padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.ic_black_circle_24),
                null,
                modifier = Modifier.size(6.dp)
            )
            Text(buildAnnotatedString {
                withStyle(style = SpanStyle(fontSize = 12.sp)) {
                    append("Thời lượng: ")
                }
                withStyle(style = SpanStyle(fontSize = 14.sp)) {
                    val duration = TimeUtils.calculateDuration(
                        cleaningJob.jobDetail.startTime,
                        cleaningJob.jobDetail.endTime
                    )
                    append(duration)
                    append(" giờ")
                }
            })
        }

        Spacer(Modifier.height(2.dp))

        Row(
            Modifier.padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.ic_black_circle_24),
                null,
                modifier = Modifier.size(6.dp)
            )
            Text(buildAnnotatedString {
                withStyle(style = SpanStyle(fontSize = 12.sp)) {
                    append("Ngày bắt đầu: ")
                }
                withStyle(style = SpanStyle(fontSize = 14.sp)) {
                    val startTime = TimeUtils.formatDateTimeFull(cleaningJob.jobDetail.startTime)
                    append(startTime)
                }
            })
        }

        Spacer(Modifier.height(2.dp))

        Row(
            Modifier.padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.ic_black_circle_24),
                null,
                modifier = Modifier.size(6.dp)
            )
            Text(buildAnnotatedString {
                withStyle(style = SpanStyle(fontSize = 12.sp)) {
                    append("Tiền lương: ")
                }
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    val price = String.format("%,.0f", cleaningJob.jobDetail.price) + " VND"
                    append(price)
                }
            }, maxLines = 1)
        }
    }
}

@Composable
fun UserInfoCard(user: UserModel) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(3.dp),
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                stringResource(R.string.user_info_title),
                fontWeight = FontWeight.W500,
                fontSize = 16.sp
            )

            Spacer(Modifier.height(8.dp))

            /*
            * Name of customer
            * */
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(R.drawable.ic_customer_service),
                    null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                )
                Spacer(Modifier.width(8.dp))
                Text(user.username, fontSize = 14.sp)
            }

            Spacer(Modifier.height(8.dp))

            /*
            * Other info of customer
            * */
            Row(verticalAlignment = Alignment.Top) {
                Spacer(Modifier.width(24.dp))
                Column(
                    Modifier.weight(1f),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    CustomUserInfoItemRow(Icons.Default.Phone, user.tel)
                    CustomUserInfoItemRow(Icons.Default.Email, user.email)
                    CustomUserInfoItemRow(Icons.Default.LocationOn, user.location)
                }
            }
        }
    }
}

@Composable
fun CustomUserInfoItemRow(icon: ImageVector, title: String = "") {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            icon,
            null,
            modifier = Modifier.size(14.dp),
            tint = colorResource(R.color.orange)
        )
        Spacer(Modifier.width(8.dp))
        Text(title, fontSize = 14.sp)
    }
}

@Preview
@Composable
fun PrevJobDetail(modifier: Modifier = Modifier) {
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

    CleaningJobDetailScreen()

}