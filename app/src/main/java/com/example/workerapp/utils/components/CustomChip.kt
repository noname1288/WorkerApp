package com.example.workerapp.utils.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.workerapp.R
import com.example.workerapp.utils.JobStatus

@Composable
fun CustomChip(status: String) {
    val color = when (status) {
        JobStatus.COMPLETED -> colorResource(R.color.green)
        JobStatus.ACCEPTED -> colorResource(R.color.blue)
        JobStatus.HIRING, JobStatus.WAITING -> colorResource(R.color.purple_500)
        else -> colorResource(R.color.red)
    }

    AssistChip(
        onClick = {},
        enabled = false,
        colors = AssistChipDefaults.assistChipColors(
            disabledLabelColor = color,
            disabledContainerColor = color.copy(alpha = 0.1f)
        ),
        label = {
            Text(
                text = JobStatus.getLabel(status),
                style = MaterialTheme.typography.labelMedium
            )
        }
    )

}