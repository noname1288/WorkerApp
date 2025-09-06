package com.example.workerapp.view.detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.workerapp.R
import com.example.workerapp.utils.button.CustomSwitch

@Composable
fun WeeklySchedule(selectedDays: List<Int> = emptyList(), isWeekly: Boolean = true) {

    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(stringResource(R.string.weekly_title), fontSize = 16.sp, fontWeight = FontWeight.W500)
        CustomSwitch(
            checked = isWeekly,
            onCheckedChange = { })
    }

    Spacer(Modifier.height(16.dp))

    val days = listOf<String>("T2", "T3", "T4", "T5", "T6", "T7", "CN")
    Row(
        Modifier
            .padding(horizontal = 8.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        days.forEachIndexed { index, day ->
            val isSelected = selectedDays.contains(index)
            DayItem(day, isSelected)
        }
    }
}

@Composable
fun DayItem(label: String, isSelected: Boolean) {
    val backgroundColor =
        if (isSelected) colorResource(R.color.orange) else colorResource(R.color.light_gray)
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
