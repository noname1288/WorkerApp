package com.example.workerapp.ui.detail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.workerapp.R
import com.example.workerapp.utils.button.CustomToggleButton
import com.example.workerapp.utils.components.DateDialog
import com.example.workerapp.utils.components.InformationItem

@Composable
fun WeeklySchedule(
    selectedDays: List<String> = emptyList(),
    isWeekly: Boolean = true,
) {
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        DateDialog(
            data = selectedDays,
            onDismissAction = { showDialog = false }
        )
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(R.color.white)
        ),
        elevation = CardDefaults.cardElevation(3.dp),
    ) {
        Column(
            Modifier.padding(16.dp)
        ) {
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    stringResource(R.string.weekly_title),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
                CustomToggleButton(
                    checked = isWeekly,
                    onCheckedChange = { })
            }

            Spacer(Modifier.height(4.dp))

            selectedDays.forEachIndexed { index, string ->
                InformationItem("Ngày ${index + 1}", string)
            }

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = { showDialog = true },
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.green).copy(
                        alpha = 0.2f
                    )
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "Hiển thị trên lịch",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = colorResource(R.color.green)
                    )
                )
            }
        }
    }
}
