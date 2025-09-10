package com.example.workerapp.utils.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.workerapp.R

@Composable
fun InformationDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
) {

    AlertDialog(
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            Button(
                onClick = {
                    onDismissRequest()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.green).copy(alpha = 0.2f)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("OK", color = colorResource(R.color.green))
            }
        },
        title = {
            Text(
                stringResource(R.string.detail_information),
                style = MaterialTheme.typography.titleLarge.copy(
                    color = colorResource(R.color.orange_primary),
                    fontWeight = FontWeight.Bold
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = {
            Column {
                InformationItem("Thể loại", "Dọn dẹp")
                HorizontalDivider()

                InformationItem("Tên khách hàng", "Lich hoc")
                HorizontalDivider()

                InformationItem("Số điện thoại", "Lich hoc")
                HorizontalDivider()

                InformationItem(
                    "Địa chỉ",
                    "When the user clicks either of the buttons, the dialog closes. When the user clicks confirm, it calls a function that also hand"
                )
                HorizontalDivider()

                InformationItem("Ngày bắt đầu", "19/08/2024")
                HorizontalDivider()

                InformationItem("Thời gian bắt đầu", "08:00")
                HorizontalDivider()

                InformationItem("Thời gian kết thúc", "12:00")
            }
        },
        containerColor = Color.White
    )
}

@Composable
fun InformationItem(label: String, value: String) {
    Column {
        Spacer(Modifier.height(8.dp))
        Row(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(
                label,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.Black
                ),
            )
            Spacer(Modifier.width(20.dp))
            Text(
                value,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = colorResource(R.color.subtext)
                ),
                maxLines = 2,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.End
            )
        }
    }
}

@Preview
@Composable
fun Prev2(modifier: Modifier = Modifier) {
    InformationDialog(onDismissRequest = {})
}

