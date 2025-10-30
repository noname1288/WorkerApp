package com.example.workerapp.presentation.screens.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.workerapp.R
import com.example.workerapp.data.source.model.base.UserModel
import com.example.workerapp.data.source.model.maintenance.MaintenanceJobResponse
import com.example.workerapp.data.source.remote.dto.wrapper.MaintenanceServiceDto
import com.example.workerapp.data.source.remote.dto.wrapper.PowerDto
import com.example.workerapp.ui.home.components.MetaRow
import com.example.workerapp.ui.theme.AppColors
import com.example.workerapp.utils.components.CustomChip
import com.example.workerapp.utils.ext.toVND

@Composable
fun MaintenanceJobCard(
    job: MaintenanceJobResponse,
    onClick: () -> Unit = {}
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        onClick = onClick
    ) {
        Column(Modifier.padding(16.dp)) {
            Image(
                painterResource(R.drawable.img_maintenance_service),
                contentDescription = "Maintenance Service Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(Modifier.height(20.dp))

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = job.location,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                    maxLines = 2,
                    modifier = Modifier.weight(1f)
                )

                CustomChip(status = job.status)
            }

            Spacer(Modifier.height(8.dp))

            // Meta rows
            MetaRow(Icons.Outlined.PersonOutline, "${job.user.username}")
            MetaRow(Icons.Outlined.Event, "Ngày làm " + job.listDays[0])
            MetaRow(Icons.Outlined.AccessTime, "Bắt đầu lúc " + job.startTime)


            Spacer(Modifier.height(4.dp))

            HorizontalDivider(color = AppColors.Divider, thickness = 1.dp)

            Spacer(Modifier.height(4.dp))

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    job.price.toVND(),
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = colorResource(R.color.orange_primary)
                    )
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMaiJobCard(modifier: Modifier = Modifier) {

    val instance = MaintenanceJobResponse(
        uid = "OZH00M1FBDVuksGsWnDp", user = UserModel(
            uid = "1V7M4UearWduxecpeigS9yXlxpv2",
            username = "Phạm Thanh Sơn",
            gender = "Nam",
            dob = "23/04/2003",
            avatar = "https://res.cloudinary.com/dvofgx21o/image/upload/v1758980280/jobs/qkgqspzzz0exomvcf8eo.png",
            email = "sonpt2304@gmail.com",
            tel = "0395770993",
            location = "Ng. 285 Khuất Duy Tiến",
            role = "user"
        ),
        serviceType = "MAINTENANCE",
        price = 106000.0,
        status = "Hiring",
        listDays = listOf(
            "21/10/2025",
            "22/10/2025",
            "23/10/2025",
            "20/10/2025"
        ),
        createdAt = "08/10/2025",
        startTime = "16:09",
        location = "Ng. 285 Khuất Duy Tiến",
        services = listOf(
            MaintenanceServiceDto(
                uid = "Pntsvw5ILpxwdO7e1Gyg",
                powers = listOf(
                    PowerDto( // Assuming the class is named PowerModel
                        uid = "ho4Igxp5I6FOXwJ7nleM",
                        quantity = 2,
                        quantityAction = 1
                    ),
                    PowerDto(
                        uid = "yaTNIssSIUbIF9Avc6rg",
                        quantity = 3,
                        quantityAction = 0
                    )
                )
            ),
            MaintenanceServiceDto(
                uid = "O45WyERwfZsJxywdbDHR",
                powers = listOf(
                    PowerDto(
                        uid = "5Dqdejv4R9FgL6Hic11f",
                        quantity = 1,
                        quantityAction = 0
                    )
                )
            )
        )
    )

    MaintenanceJobCard(job = instance)
}