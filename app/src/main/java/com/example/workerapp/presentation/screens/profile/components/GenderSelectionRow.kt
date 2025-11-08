package com.example.workerapp.presentation.screens.profile.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun GenderSelectionRow(
    modifier: Modifier = Modifier,
    title: String = "Giới tính",
    genderList: List<String> = listOf("Nam", "Nữ", "Khác"),
    selectedGender: String,
    onGenderSelected: (String) -> Unit
) {
    Column(modifier = modifier) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            genderList.forEach { gender ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clickable { onGenderSelected(gender) }
                        .padding(horizontal = 4.dp)
                ) {
                    RadioButton(
                        selected = gender == selectedGender,
                        onClick = { onGenderSelected(gender) }
                    )
                    Text(
                        text = gender,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = if (gender == selectedGender) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onSurface
                        )
                    )

                    Spacer(Modifier.width(8.dp))
                }
            }
        }
    }
}
