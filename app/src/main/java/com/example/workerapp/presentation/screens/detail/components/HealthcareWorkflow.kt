package com.example.workerapp.presentation.screens.detail.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import com.example.workerapp.R
import com.example.workerapp.data.source.model.healthcare.HealthcareServiceModel
import com.example.workerapp.data.source.remote.dto.wrapper.HealthServiceWrapper

@Composable
fun HealthcareServiceItem(
    service: HealthcareServiceModel,
    quantity: Int,
) {

    var showDialog by remember { mutableStateOf(false) }

    val image = when (service.serviceName){
        "Trẻ em" -> R.drawable.childcare
        "Người lớn tuổi" -> R.drawable.eldercare
        "Người khuyết tật" -> R.drawable.wheelchaircare
        else -> R.drawable.ic_launcher_background
    }

    if (showDialog) {
        HealthcareServiceDetailDialog(
            item = service,
            onDismissRequest = { showDialog = false }
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Image
            Image(
                painter = painterResource(image),
                contentDescription = "Healthcare Service",
                modifier = Modifier
                    .size(height = 90.dp, width = 120.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Text content
            Column(
                modifier = Modifier.weight(1f),
            ) {
                Text(
                    text = service.serviceName,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "Số lượng: $quantity",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Danh sách công việc",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontStyle = FontStyle.Italic,
                        color = colorResource(R.color.green)
                    ),
                    modifier = Modifier.clickable { showDialog = true }
                )
            }
        }
    }
}

@Composable
fun HealthcareServiceDetailDialog(item: HealthcareServiceModel, onDismissRequest: () -> Unit) {
    AlertDialog(
        onDismissRequest = { onDismissRequest() },
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
                stringResource(R.string.scope_of_work),
                style = MaterialTheme.typography.titleLarge.copy(
                    color = colorResource(R.color.orange_primary),
                    fontWeight = FontWeight.Bold
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    "Công việc nên làm", style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )

                item.duties.forEach { duty ->
                    Text(
                        "• $duty",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                }

                Text(
                    "Công việc không làm", style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )

                item.excludedTasks.forEach { nonDuty ->
                    Text(
                        "• $nonDuty",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                }
            }
        },
        containerColor = Color.White
    )
}
