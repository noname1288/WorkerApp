package com.example.workerapp.presentation.screens.profile.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun NewTextFieldRow(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    title: String = "Hello work",
    placeholderText: String = "",
    readOnly : Boolean = false,
    enabled : Boolean = true,
    trailingIcon: @Composable (() -> Unit)? = null,
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors()
) {

    Column(
        horizontalAlignment = Alignment.Start
    ) {
        Text(title, style = MaterialTheme.typography.titleMedium)

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = value,
            onValueChange = { onValueChange(it) },
            placeholder = { Text(placeholderText) },
            trailingIcon = trailingIcon,
            singleLine = true,
            readOnly = readOnly,
            enabled = enabled,
            colors = colors,
            modifier = modifier.fillMaxWidth()
        )
    }
}
