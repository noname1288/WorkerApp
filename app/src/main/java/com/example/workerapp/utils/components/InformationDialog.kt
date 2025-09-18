package com.example.workerapp.utils.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.workerapp.R
import com.example.workerapp.data.source.model.base.JobModel1
import com.example.workerapp.utils.ServiceType

@Composable
fun InformationDialog(
    modifier: Modifier = Modifier,
    job: JobModel1,
    onDismissRequest: () -> Unit,
) {
    val serviceType = when (job.serviceType){
        ServiceType.CleaningType -> "Dọn dẹp"
        ServiceType.HealthcareType -> "Chăm sóc sức khỏe"
        ServiceType.MaintenanceType -> "Bảo trì"
        else -> "Khác"
    }

    val days = job.listDays.joinToString (separator = "\n")

    AlertDialog(
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            Button(
                onClick = {
                    onDismissRequest()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.green).copy(alpha = 0.2f)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("OK", color = colorResource(R.color.green))
            }
        },
        title = {
            Text(
                stringResource(R.string.detail_information),
                style = MaterialTheme.typography.titleLarge.copy(
                    color = colorResource(R.color.orange_primary),
                    fontWeight = FontWeight.Bold
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = {
            Column {
                InformationItem("Thể loại", serviceType)
                HorizontalDivider()

                InformationItem("Tên khách hàng", job.user.username)
                HorizontalDivider()

                InformationItem("Số điện thoại", job.user.tel)
                HorizontalDivider()

                InformationItem(
                    "Địa chỉ",
                    job.location
                )
                HorizontalDivider()

                InformationItem("Ngày làm việc", days)
                HorizontalDivider()

                InformationItem("Thời gian bắt đầu", job.startTime)
                HorizontalDivider()

                InformationItem("Lương", "${job.price} VND", true)
            }
        },
        containerColor = Color.White,
    )
}

@Composable
fun InformationItem(label: String, value: String, isImportant: Boolean = false) {
    Column {
        Spacer(Modifier.height(8.dp))
        Row(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(
                label,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.Black
                ),
            )
            Spacer(Modifier.width(20.dp))
            Text(
                value,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = if (isImportant) colorResource(R.color.orange_primary) else colorResource(
                        R.color.subtext
                    ),
                    fontWeight = if (isImportant) FontWeight.Bold else FontWeight.Normal
                ),
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.End
            )
        }
    }
}

