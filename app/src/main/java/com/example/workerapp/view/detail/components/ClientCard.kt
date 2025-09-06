package com.example.workerapp.view.detail.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.workerapp.R
import com.example.workerapp.data.model.base.UserModel
import com.example.workerapp.view.components.InfoItemRow

@Composable
fun ClientCard(user: UserModel) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(3.dp),
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                stringResource(R.string.user_info_title),
                fontWeight = FontWeight.W500,
                fontSize = 16.sp
            )

            Spacer(Modifier.height(8.dp))

            /*
            * Name of customer
            * */
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(R.drawable.ic_customer_service),
                    null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                )
                Spacer(Modifier.width(8.dp))
                Text(user.username, fontSize = 14.sp)
            }

            Spacer(Modifier.height(8.dp))

            /*
            * Other info of customer
            * */
            Row(verticalAlignment = Alignment.Top) {
                Spacer(Modifier.width(24.dp))
                Column(
                    Modifier.weight(1f),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    InfoItemRow(Icons.Default.Phone, user.tel)
                    InfoItemRow(Icons.Default.Email, user.email)
                    InfoItemRow(Icons.Default.LocationOn, user.location)
                }
            }
        }
    }
}


