package com.example.workerapp.presentation.screens.profile.components

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.workerapp.R

@Composable
fun CustomExtendedButton(
    modifier: Modifier = Modifier,
    label: String,
    leadingIcon: ImageVector = Icons.Default.Face,
    trailingIcon: ImageVector = Icons.Default.ArrowForwardIos,
    onClick: () -> Unit
) {
    Button(
        onClick = { onClick() },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(0.dp),
        contentPadding = PaddingValues(16.dp, 20.dp)
    ) {
        Icon(
            leadingIcon,
            null,
            tint = colorResource(R.color.color_icon),
            modifier = Modifier.size(24.dp)
        )

        Spacer(Modifier.width(16.dp))

        Text(
            label, color = Color.Black,
            style = MaterialTheme.typography.bodyMedium,
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
