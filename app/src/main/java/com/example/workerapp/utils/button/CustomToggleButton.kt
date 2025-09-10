package com.example.workerapp.utils.button

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.workerapp.R

@Composable
fun CustomToggleButton(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    val backgroundColor by animateColorAsState(
        if (checked) colorResource(R.color.light_orange_icon) else Color.LightGray
    )
    val alignment by animateDpAsState(
        if (checked) 20.dp else 0.dp
    )

    Box(
        modifier = Modifier
            .width(36.dp)
            .height(16.dp)
            .clip(CircleShape)
            .background(backgroundColor)
            .clickable { onCheckedChange(!checked) },
        contentAlignment = Alignment.CenterStart
    ) {
        //thumb draggle
        Box(
            modifier = Modifier
                .offset(x = alignment)
                .size(16.dp)
                .clip(CircleShape)
                .background(Color.White)
                .border(1.dp,colorResource(R.color.light_orange_icon), CircleShape)
        )
    }
}
