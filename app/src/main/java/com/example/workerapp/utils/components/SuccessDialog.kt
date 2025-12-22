package com.example.workerapp.utils.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.workerapp.R

@Composable
fun SuccessDialog(
    content: String = "",
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = { onDismiss() },
        content = {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            )
            {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    //icon
                    Image(
                        painterResource(R.drawable.ic_success),
                        "Success",
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(Modifier.height(12.dp))

                    Text(
                        "Thành công",
                        style = TextStyle(
                            fontSize = 24.sp, fontWeight = FontWeight.Bold
                        )
                    )
                    Spacer(Modifier.height(4.dp))

                    Text(
                        content,
                        style = TextStyle(
                            fontSize = 16.sp
                        )
                    )

                    Spacer(Modifier.height(12.dp))
                    //Confirm Button
                    Button(
                        onClick = { onDismiss() },
                        modifier = Modifier.width(150.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.green))
                    ) {
                        Text("OK")
                    }
                }
            }
        }
    )
}
