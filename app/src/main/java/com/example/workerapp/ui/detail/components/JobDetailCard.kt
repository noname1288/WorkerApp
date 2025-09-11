package com.example.workerapp.ui.detail.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.workerapp.R
import com.example.workerapp.data.model.CleaningJobModel
import com.example.workerapp.data.model.base.JobModel
import com.example.workerapp.utils.components.InformationItem

@SuppressLint("DefaultLocale")
@Composable
fun JobDetailCard(cleaningJob: CleaningJobModel) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(R.color.white)
        ),
        elevation = CardDefaults.cardElevation(3.dp),
        ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                stringResource(R.string.job_detail_title_2),
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )

            InformationItem("Danh mục", value = "${cleaningJob.jobDetail.serviceType}")
            InformationItem("Thời lượng", value = "08:00 - 12:00 [3 giờ]")
            InformationItem("Ngày bắt đầu", value = "22/09/2024")
            InformationItem("Ngày kết thúc", value = "22/09/2024")
        }
    }
}

@Preview
@Composable
fun PrevJobDetailCard(modifier: Modifier = Modifier) {
    val job = CleaningJobModel(
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
    JobDetailCard(job)
}