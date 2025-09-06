package com.example.workerapp.view.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.workerapp.R

@Composable
fun InfoItemRow(
    icon: ImageVector,
    title: String = "",
    iconSize: Dp = 16.dp,
    width: Dp = 8.dp,
    fontSize: TextUnit = 14.sp) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            icon,
            null,
            modifier = Modifier.size(iconSize),
            tint = colorResource(R.color.light_orange_icon)
        )
        Spacer(Modifier.width(width))
        Text(title, fontSize = fontSize, maxLines = 1)
    }
}
