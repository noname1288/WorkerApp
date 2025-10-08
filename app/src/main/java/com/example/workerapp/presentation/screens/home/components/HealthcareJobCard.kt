package com.example.workerapp.ui.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Accessibility
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.workerapp.R
import com.example.workerapp.data.source.model.healthcare.HealthcareJobModel
import com.example.workerapp.ui.theme.AppColors
import com.example.workerapp.utils.ext.toVND

@Composable
fun HealthcareJobCard(job: HealthcareJobModel, modifier: Modifier = Modifier, onClick: () -> Unit = {}) {
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
            Row() {
                // Left icon block
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .background(colorGreen.copy(alpha = 0.08f), RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        iconJob,
                        contentDescription = null,
                        tint = colorGreen,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(Modifier.width(16.dp))

                Column(Modifier.weight(1f)) {
                    Spacer(Modifier.height(8.dp))

                    Text(
                        text = job.location,
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                    )

                    Spacer(Modifier.height(8.dp))

                    // Meta rows
                    MetaRow(Icons.Outlined.PersonOutline, "Khách hàng: ${job.user.username}")
                    MetaRow(Icons.Outlined.Accessibility, "Giới tính: ${job.user.gender}")
                    MetaRow(Icons.Outlined.Event, "Ngày: " + job.listDays[0] + "  ·  " + job.startTime)
                    MetaRow(Icons.Outlined.Schedule, "Tối đa: ${job.shift.workingHour} giờ")
                }
            }

            Spacer(Modifier.height(4.dp))

            HorizontalDivider(color = AppColors.Divider, thickness = 1.dp)

            Spacer(Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 16.dp)) {
                Spacer(Modifier.weight(1f))
                Text(
                    text = job.price.toVND(),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = AppColors.Primary
                )
            }

        }
    }
}

@Composable
private fun MetaRow(image: ImageVector, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 2.dp)
    ) {
        Icon(
            image,
            contentDescription = null,
            tint = colorResource(R.color.subtext),
            modifier = Modifier.size(18.dp)
        )
        Spacer(Modifier.width(8.dp))
        Text(text, style = MaterialTheme.typography.bodyMedium, color = colorResource(R.color.subtext))
    }
}

