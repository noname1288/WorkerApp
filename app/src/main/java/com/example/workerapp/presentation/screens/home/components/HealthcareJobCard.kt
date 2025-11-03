package com.example.workerapp.presentation.screens.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Accessibility
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.workerapp.R
import com.example.workerapp.data.source.model.healthcare.HealthcareJobModel
import com.example.workerapp.ui.home.components.MetaRow
import com.example.workerapp.ui.theme.AppColors
import com.example.workerapp.utils.components.CustomChip
import com.example.workerapp.utils.ext.toVND

@Composable
fun HealthcareJobCard(
    job: HealthcareJobModel,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val iconJobInt = R.drawable.ic_healthcare_64
    val iconJob = painterResource(iconJobInt)

    val colorGreen = colorResource(R.color.green)

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        onClick = onClick
    ) {
        Column(Modifier.padding(16.dp)) {
            Image(
                painterResource(R.drawable.img_healthcare_service),
                contentDescription = "Healthcare Service Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(Modifier.height(20.dp))

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = job.location,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                    maxLines = 2,
                    modifier = Modifier.weight(1f)
                )

                CustomChip(status = job.status)
            }

            Spacer(Modifier.height(8.dp))

            // Meta rows
            MetaRow(Icons.Outlined.PersonOutline, "Khách hàng: ${job.user.username}")
            MetaRow(Icons.Outlined.Accessibility, "Giới tính: ${job.user.gender}")
            MetaRow(Icons.Outlined.Event, "Ngày: " + job.listDays[0] + "  ·  " + job.startTime)
            MetaRow(Icons.Outlined.Schedule, "Tối đa: ${job.shift.workingHour} giờ")


            Spacer(Modifier.height(4.dp))

            HorizontalDivider(color = AppColors.Divider, thickness = 1.dp)

            Spacer(Modifier.height(4.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Spacer(Modifier.weight(1f))
                Text(
                    text = job.price.toVND(),
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = AppColors.Primary
                )
            }

        }
    }
}

