package com.example.workerapp.view.detail.components

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.workerapp.R
import com.example.workerapp.data.model.CleaningJobModel
import com.example.workerapp.utils.TimeUtils

@SuppressLint("DefaultLocale")
@Composable
fun JobDetailCard(cleaningJob: CleaningJobModel) {
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