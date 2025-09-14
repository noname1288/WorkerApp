package com.example.workerapp.ui.detail.healcare

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.workerapp.R
import com.example.workerapp.data.model.base.UserModel
import com.example.workerapp.data.model.healthcare.HealthcareJobModel
import com.example.workerapp.ui.detail.components.ClientCard
import com.example.workerapp.ui.detail.components.JobDetailCard
import com.example.workerapp.ui.detail.components.WeeklySchedule
import com.example.workerapp.utils.button.SlideToConfirmButton
import com.example.workerapp.utils.components.CircleLoadingIndicator

sealed class HealthcareJobSection {
    data class UserInfo(val user: UserModel) : HealthcareJobSection()
    data class JobDetails(val job: HealthcareJobModel) : HealthcareJobSection()
    data class WeeklySchedule(val days: List<String>, val isWeekly: Boolean) :
        HealthcareJobSection()

    //    data class JobWorkflow(val jobServiceWrapper: List<HealthServiceWrapper>) : HealthcareJobSection()
    object ActionButtons : HealthcareJobSection()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HealthcareDetailScreen(
    healthcareUid: String,
    viewModel: HealthcareViewModel,
    navController: NavController
) {
    val context = LocalContext.current
    val TAG = "HealthcareDetailScreen"

    val uiState by viewModel.uiState.collectAsState()
    var sections = listOf<HealthcareJobSection>()

    LaunchedEffect(Unit) {
        viewModel.fetchJobDetail(healthcareUid)
    }

    when (uiState) {
        is HealthcareUiState.Success -> {
            val job = (uiState as HealthcareUiState.Success).data

            sections = listOf(
                HealthcareJobSection.UserInfo(job.user),
                HealthcareJobSection.JobDetails(job),
                HealthcareJobSection.WeeklySchedule(
                    days = job.listDays,
                    isWeekly = job.listDays.size != 1
                ),
//                HealthcareJobSection.JobWorkflow(job.services),
                HealthcareJobSection.ActionButtons
            )
        }

        is HealthcareUiState.Error -> {
            Toast.makeText(context, (uiState as HealthcareUiState.Error).message, Toast.LENGTH_LONG)
                .show()
        }

        is HealthcareUiState.Loading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircleLoadingIndicator()
            }
        }

        HealthcareUiState.Idle -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Idle")
            }
        }
    }

    Column {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    stringResource(R.string.job_detail_title),
                    fontWeight = FontWeight.Bold
                )
            },
            windowInsets = WindowInsets(0, 0, 0, 0),
            navigationIcon = {
                IconButton(onClick = {}) {
                    Icon(
                        Icons.Default.ArrowBackIosNew, contentDescription = "Back",
                        modifier = Modifier.size(20.dp)
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
        )

        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            item {
                Spacer(Modifier.height(16.dp))
            }

            sections.forEach { section ->
                when (section) {
                    is HealthcareJobSection.UserInfo -> {
                        item {
                            ClientCard(user = section.user)
                            Spacer(Modifier.height(12.dp))
                        }
                    }

                    is HealthcareJobSection.JobDetails -> {
                        item {
                            JobDetailCard(section.job)
                            Spacer(Modifier.height(12.dp))
                        }
                    }

                    is HealthcareJobSection.WeeklySchedule -> {
                        item {
                            WeeklySchedule(section.days)
                            Spacer(Modifier.height(12.dp))
                        }
                    }

//                    is HealthcareJobSection.JobWorkflow -> {
//                        item {
//                            HealthcareWorkflow(section.jobServiceWrapper)
//                            Spacer(Modifier.height(24.dp))
//                        }
//                    }

                    is HealthcareJobSection.ActionButtons -> {
                        item {
                            SlideToConfirmButton(
                                onConfirmed = {
                                    Log.d(TAG, "HealthDetailScreen: Confirmed")
                                }
                            )
                            Spacer(Modifier.height(24.dp))
                        }
                    }
                }
            }
        }
    }

}
