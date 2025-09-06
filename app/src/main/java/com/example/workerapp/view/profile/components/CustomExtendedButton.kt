package com.example.workerapp.view.profile.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.workerapp.R

@Composable
fun CustomExtendedButton(
    modifier: Modifier = Modifier,
    label: String,
    fontSize: TextUnit = 14.sp,
    leadingIcon: ImageVector = Icons.Default.Face,
    trailingIcon: ImageVector = Icons.Default.ArrowForwardIos,
    onClick: () -> Unit
) {
    Button(
        onClick = { onClick() },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,

            ),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, Color.LightGray),
        contentPadding = PaddingValues(24.dp, 12.dp)
    ) {
        Icon(leadingIcon,
            null,
            tint = colorResource(R.color.light_orange_icon),
            modifier = Modifier.size(22 .dp))

        Spacer(Modifier.width(20.dp))

        Text(
            label, color = Color.Black, fontSize = fontSize,
            fontWeight = FontWeight.Normal
        )

        Spacer(Modifier.weight(1f))

        Icon(
            trailingIcon,
            null,
            tint = colorResource(R.color.black),
            modifier = modifier.size(16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PrevCustomExtendedButton() {
    CustomExtendedButton(
        label = "Click Me",
        leadingIcon = Icons.Default.Face,
        trailingIcon = Icons.Default.ArrowForwardIos
    ) {}
}
