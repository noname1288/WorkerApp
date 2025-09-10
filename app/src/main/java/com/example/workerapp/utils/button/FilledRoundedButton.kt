package com.example.workerapp.utils.button

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.workerapp.R

@Composable
fun FilledRoundedButton(
    modifier: Modifier = Modifier,
    label: String = "Phong an",
    isSelected: Boolean = false,
    onClick: () -> Unit
) {
    Button(
        onClick = { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor =
                if (isSelected) colorResource(R.color.light_orange_icon) else colorResource(R.color.light_gray),
            contentColor = if (isSelected) Color.White else Color.Black
        )
    ) {
        Text(label)
    }
}
