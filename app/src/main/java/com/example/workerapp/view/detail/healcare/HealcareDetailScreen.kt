package com.example.workerapp.view.detail.healcare

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.workerapp.R
import com.example.workerapp.data.model.HealthcareJobModel
import com.example.workerapp.data.model.base.UserModel
import com.example.workerapp.view.detail.components.WeeklySchedule
import com.example.workerapp.view.detail.components.JobWorkflow
import com.example.workerapp.view.detail.components.ClientCard

sealed class HealthcareJobSection {
    data class UserInfo(val user: UserModel) : HealthcareJobSection()
    data class JobDetails(val job: HealthcareJobModel) : HealthcareJobSection()
    data class WeeklySchedule(val days: List<Int>, val isWeekly: Boolean) : HealthcareJobSection()
    object JobWorkflow : HealthcareJobSection()
    object ActionButtons : HealthcareJobSection()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HealthcareDetailScreen(modifier: Modifier = Modifier) {
    val fakeUser = UserModel(
        username = "Phạm Thanh Sơn",
        gender = "Male",
        dob = "1990-01-01",
        avatar = "https://example.com/avatar.jpg",
        tel = "1234567890",
        location = "New York",
        email = "john.doe@example.com",
        role = "user"
    )
    val fakeDays = listOf(2, 3, 4)

    var isShowBottomSheet by remember { mutableStateOf(false) }

    val sections = listOf(
        HealthcareJobSection.UserInfo(fakeUser),
        HealthcareJobSection.WeeklySchedule(fakeDays, isWeekly = true),
        HealthcareJobSection.JobWorkflow,
        HealthcareJobSection.ActionButtons
    )

    Column {
        TopAppBar(
            title = { Text("Chi tiết công việc") },
            navigationIcon = {
                IconButton(onClick = {}) {
                    Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = colorResource(R.color.light_orange)
            )
        )
        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            item{
                Spacer(Modifier.height(24.dp))
            }

            sections.forEach { section ->
                when(section){
                    is HealthcareJobSection.UserInfo -> {
                        item{
                            ClientCard(user = section.user)
                            Spacer(Modifier.height(24.dp))
                        }
                    }
                    is HealthcareJobSection.WeeklySchedule -> {
                        item {
                            WeeklySchedule(section.days)
                            Spacer(Modifier.height(48.dp))
                        }
                    }
                    is HealthcareJobSection.JobWorkflow -> {
                        item {
                            JobWorkflow(onClick = { isShowBottomSheet = true })
                            Spacer(Modifier.height(24.dp))
                        }
                    }
                    is HealthcareJobSection.ActionButtons -> {
                        item {
                            Spacer(Modifier.height(32.dp))
                        }
                    }
                    else -> {}
                }
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun HealthcareDetailScreenPreview() {
    HealthcareDetailScreen()
}