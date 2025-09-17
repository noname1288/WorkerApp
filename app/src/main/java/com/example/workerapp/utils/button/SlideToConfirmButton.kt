package com.example.workerapp.utils.button

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.workerapp.R

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun SlideToConfirmButton(
    modifier: Modifier = Modifier,
    isConfirmed: Boolean,
    onValueChange: (Boolean) -> Unit,
) {
    val thumbSize = 56.dp
    var offsetX by remember { mutableStateOf(0f) }
    val density = LocalDensity.current

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth() // full width màn hình
            .height(thumbSize)
            .clip(RoundedCornerShape(20.dp))
            .background(Color.LightGray),
        contentAlignment = Alignment.CenterStart
    ) {
        val maxDragPx = with(density) { (maxWidth - thumbSize).toPx() }

        // Khi confirmed = true thì ép thumb về cuối
        LaunchedEffect(isConfirmed) {
            if (isConfirmed) offsetX = maxDragPx
        }

        // phần nền màu fill theo offset
        Box(
            modifier = Modifier
                .matchParentSize()
                .clip(RoundedCornerShape(20.dp))
                .drawBehind {
                    val progress = (offsetX / maxDragPx).coerceIn(0f, 1f)
                    val fillWidth = size.width * progress
                    drawRoundRect(
                        color = if (isConfirmed) Color(0xFF4CAF50) else Color(0xFFF8A66E),
                        size = Size(fillWidth, size.height),
                        cornerRadius = CornerRadius(20.dp.toPx(), 20.dp.toPx())
                    )
                }
        )

        // text
        Text(
            text = if (isConfirmed) "Đã xác nhận!" else "Trượt để xác nhận",
            color = if (isConfirmed) Color.White else Color.DarkGray,
            modifier = Modifier.align(Alignment.Center)
        )

        // thumb draggable

        Box(
            modifier = Modifier
                .offset { IntOffset(offsetX.toInt(), 0) }
                .size(thumbSize)
                .clip(RoundedCornerShape(20.dp))
                .background(if (isConfirmed) Color.White else Color(0xFF4CAF50))
                .border(1.dp, colorResource(R.color.green), RoundedCornerShape(20.dp))
                .draggable(
                    orientation = Orientation.Horizontal,
                    state = rememberDraggableState { delta ->
                        if (!isConfirmed) {
                            offsetX = (offsetX + delta).coerceIn(0f, maxDragPx)
                        }
                    },
                    onDragStopped = {
                        val threshold = maxDragPx * 0.8f
                        if (offsetX >= threshold) {
                            onValueChange (true)
                            offsetX = maxDragPx
                        } else {
                            offsetX = 0f
                        }
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isConfirmed) {
                Icon(Icons.Default.Check, contentDescription = null, tint = Color(0xFF4CAF50))
            } else {
                Icon(Icons.Default.ArrowForward, contentDescription = null, tint = Color.White)
            }
        }
    }

}
