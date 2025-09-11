package com.example.workerapp.ui.home.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material.icons.outlined.People
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.workerapp.R
import com.example.workerapp.data.repository.remote.dto.JobResponseDto
import com.example.workerapp.ui.theme.AppColors
import com.example.workerapp.utils.ServiceType

data class JobUi(
    val categoryName: String,
    val categoryTint: Color,
    val iconRes: Int,
    val address: String,
    val date: String,   // "18/08/2025"
    val isToday: Boolean,
    val time: String,   // "14:30 - 17:00"
    val duration: String, // "3 giờ"
    val people: String, // "1 người"
    val price: String   // "100.000 VND"
)

@Composable
fun JobCard(job: JobResponseDto, modifier: Modifier = Modifier, onClick: () -> Unit = {}) {
    val iconJobInt = if (job.serviceType == ServiceType.CleaningType) R.drawable.ic_cleaning_100
                        else if (job.serviceType == ServiceType.HealthcareType) R.drawable.ic_healthcare_64
                        else R.drawable.ic_launcher_background
    val iconJob = painterResource(iconJobInt)

    val colorJobInt = if (job.serviceType == ServiceType.CleaningType) R.color.blue
                        else if (job.serviceType == ServiceType.HealthcareType) R.color.red
                        else R.color.purple_500
    val colorJob = colorResource(colorJobInt)
    val colorGreen = colorResource(R.color.green)





    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        onClick = onClick
    ) {
        Row(Modifier.padding(16.dp)) {
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

            Spacer(Modifier.width(12.dp))

            Column(Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = job.address,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = AppColors.Text,
                        modifier = Modifier.weight(1f),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(Modifier.width(8.dp))
                    CategoryChip(job.serviceType, colorGreen, selected = true, onClick = {})
                }



                Spacer(Modifier.height(8.dp))

                // Meta rows
                MetaRow(
                    Icons.Outlined.Event,
                    "17/07/2004 · Hom nay"
                )
                MetaRow(Icons.Outlined.Schedule, "14:30" + "  ·  " + "3 gio")
                MetaRow(Icons.Outlined.People, job.workerQuantity.toString() + " nguoi")

                Spacer(Modifier.height(10.dp))
                Divider(color = AppColors.Divider, thickness = 1.dp)
                Spacer(Modifier.height(10.dp))

                // Footer: actions (optional) + price
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // (Nếu cần icon action, đặt ở đây với AppColors.Muted)
                    Spacer(Modifier.weight(1f))
                    Text(
                        text = job.price.toString(),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = AppColors.Primary
                    )
                }
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
            tint = AppColors.SubText,
            modifier = Modifier.size(18.dp)
        )
        Spacer(Modifier.width(8.dp))
        Text(text, style = MaterialTheme.typography.bodyMedium, color = AppColors.SubText)
    }
}


@Composable
fun CategoryChip(label: String, tint: Color, selected: Boolean, onClick: () -> Unit) {
    val bg = if (selected) tint.copy(alpha = 0.10f) else AppColors.Divider
    val border = if (selected) tint.copy(alpha = 0.30f) else AppColors.Divider
    val textColor = if (selected) tint else AppColors.SubText

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = bg,
        border = BorderStroke(1.dp, border),
        onClick = onClick
    ) {
        Text(
            label,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            color = textColor,
            fontSize = 10.sp
        )
    }
}

@Preview
@Composable
fun prevJobCard() {
    val currentTime = System.currentTimeMillis()

    val job = JobResponseDto(
        id = "1",
        clientName = "John Doe",
        phoneNumber = "1234567890",
        address = "SN02 ngõ 38, Mộ Lao, Hà Đông, Hà Nội",
        workerQuantity = 5,
        serviceType = "Dọn dẹp",
        startTime = currentTime + 3600000, // 1 hour from now
        endTime = currentTime + 5400000, // 1.5 hours from now
        price = 150000.0
    )

    JobCard(job = job) {

    }
}