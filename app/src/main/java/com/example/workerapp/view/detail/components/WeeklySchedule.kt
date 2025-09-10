package com.example.workerapp.view.detail.components

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.workerapp.R
import com.example.workerapp.utils.button.CustomToggleButton
import com.example.workerapp.utils.components.InformationItem

@Composable
fun WeeklySchedule(
    selectedDays: List<Int> = emptyList(),
    isWeekly: Boolean = true,
    onClick: () -> Unit = {}
) {
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

            InformationItem("Ngày 1", "22/09/2024")
            InformationItem("Ngày 2", "22/09/2024")
            InformationItem("Ngày 3", "22/09/2024")

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = { onClick() },
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

@Composable
fun DayItem(label: String, isSelected: Boolean) {
    val backgroundColor =
        if (isSelected) colorResource(R.color.orange_primary) else colorResource(R.color.light_gray)
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

@Preview(showBackground = true)
@Composable
fun WeeklySchedulePreview() {
    WeeklySchedule(selectedDays = listOf(0, 2, 4), isWeekly = true)
}

