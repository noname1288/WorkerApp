package com.example.workerapp.presentation.screens.forgot_password

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Alignment
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.material3.Text

@Composable
fun OtpInputField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    isFocused: Boolean = false
) {
    Box(
        modifier = modifier
            .size(48.dp)
            .border(
                width = 1.5.dp,
                color = if (isFocused) Color(0xFF3F51B5) else Color.LightGray,
                shape = RoundedCornerShape(8.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        BasicTextField(
            value = value,
            onValueChange = {
                if (it.length <= 1 && it.all(Char::isDigit)) onValueChange(it)
            },
            textStyle = TextStyle(
                textAlign = TextAlign.Center,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            cursorBrush = SolidColor(Color.Black),
            decorationBox = { innerTextField ->
                if (value.isEmpty()) {
                    Text(
                        text = "",
                        style = TextStyle(color = Color.Gray, textAlign = TextAlign.Center)
                    )
                }
                innerTextField()
            }
        )
    }
}
