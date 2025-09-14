package com.example.workerapp.utils.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.workerapp.R
import com.example.workerapp.utils.TimeUtils
import java.time.YearMonth


@Composable
fun DateDialog(data: List<String>, onDismissAction: () -> Unit) {
    val months = TimeUtils.groupDates(data)

    AlertDialog(
        onDismissRequest = { onDismissAction() },
        confirmButton = {
            Button(
                onClick = {
                    onDismissAction()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.green).copy(alpha = 0.2f)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("OK", color = colorResource(R.color.green))
            }
        },
        text = {
            LazyColumn(modifier = Modifier.heightIn(max = 500.dp)) {
                items(months) { monthData ->
                    Text(
                        text = "${monthData.month.monthValue}/${monthData.month.year}",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    MonthCalendar(monthData)
                }
            }
        },
        containerColor = Color.White,
    )
}

@Composable
fun MonthCalendar(monthData: MonthWithDays) {
    val firstDayOfMonth = monthData.month.atDay(1)
    val daysInMonth = monthData.month.lengthOfMonth()
    val dayOfWeekOffset = (firstDayOfMonth.dayOfWeek.value % 7) // để CN = 0

    val totalCells = daysInMonth + dayOfWeekOffset
    val rows = (totalCells + 6) / 7

    Column {
        // header thứ
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            listOf("CN", "T2", "T3", "T4", "T5", "T6", "T7").forEach {
                Text(
                    text = it,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    fontSize = 12.sp
                )
            }
        }

        Spacer(Modifier.height(4.dp))

        // grid ngày
        for (row in 0 until rows) {
            Row(Modifier.fillMaxWidth()) {
                for (col in 0..6) {
                    val dayIndex = row * 7 + col
                    val dayNumber = dayIndex - dayOfWeekOffset + 1
                    if (dayNumber in 1..daysInMonth) {
                        val isHighlighted = monthData.highlighted.contains(dayNumber)
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .padding(2.dp)
                                .background(
                                    if (isHighlighted) Color(0xFFFF9800)
                                    else Color.Transparent,
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = dayNumber.toString(),
                                color = if (isHighlighted) Color.White else Color.Black
                            )
                        }
                    } else {
                        Spacer(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                        )
                    }
                }
            }
        }
    }
}

data class MonthWithDays(
    val month: YearMonth,
    val highlighted: Set<Int>
)
