package com.example.workerapp.presentation.screens.detail.healcare

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
import androidx.compose.material.icons.filled.Map
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.workerapp.MyApplication
import com.example.workerapp.R
import com.example.workerapp.data.source.model.base.UserModel
import com.example.workerapp.data.source.model.healthcare.HealthcareJobModel
import com.example.workerapp.data.source.model.healthcare.HealthcareServiceModel
import com.example.workerapp.presentation.screens.detail.components.HealthcareServiceItem
import com.example.workerapp.ui.detail.components.ClientCard
import com.example.workerapp.ui.detail.components.JobDetailCard
import com.example.workerapp.ui.detail.components.WeeklySchedule
import com.example.workerapp.utils.button.SlideToConfirmButton
import com.example.workerapp.utils.components.CircleLoadingIndicator
import com.example.workerapp.utils.ext.openGoogleMap
import com.example.workerapp.utils.ext.popBackIfCan

sealed class HealthcareJobSection {
    data class UserInfo(val user: UserModel) : HealthcareJobSection()
    data class JobDetails(val job: HealthcareJobModel) : HealthcareJobSection()
    data class WeeklySchedule(val days: List<String>, val isWeekly: Boolean) :
        HealthcareJobSection()

    data class JobWorkflow(val serviceData: List<Pair<HealthcareServiceModel, Int>>) :
        HealthcareJobSection()

    object ActionButtons : HealthcareJobSection()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HealthcareDetailScreen(
    modifier: Modifier = Modifier,
    healthcareUid: String,
    isOnlyWatch: Boolean = false,
    viewModel: HealthcareViewModel,
    navController: NavController
) {
    val context = LocalContext.current
    val app = context.applicationContext as MyApplication
    val tag = "HealthcareDetailScreen"

    var sections = listOf<HealthcareJobSection>()

    val uiState by viewModel.uiState.collectAsState()
    val applyState by viewModel.applyState.collectAsState()
    var confirmed by rememberSaveable { mutableStateOf(false) }
    var jobAddress by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(Unit, applyState) {
        when (applyState) {
            true -> {
                Toast.makeText(context, "Ứng tuyển thành công!", Toast.LENGTH_LONG).show()
                navController.popBackIfCan()
            }

            false -> {
                viewModel.updateApplyState(null)
                confirmed = false
            }

            else -> {
                viewModel.fetchJobDetail(healthcareUid)
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.fetchJobDetail(healthcareUid)
    }

    when (uiState) {
        is HealthcareUiState.Success -> {
            val job = (uiState as HealthcareUiState.Success).data
            val serviceData = (uiState as HealthcareUiState.Success).serviceData
            jobAddress = job.location

            sections = listOf(
                HealthcareJobSection.UserInfo(job.user),
                HealthcareJobSection.JobDetails(job),
                HealthcareJobSection.WeeklySchedule(
                    days = job.listDays,
                    isWeekly = job.listDays.size != 1
                ),
                HealthcareJobSection.JobWorkflow(serviceData),
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

    Column(modifier.fillMaxSize()) {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    stringResource(R.string.job_detail_title),
                    fontWeight = FontWeight.Bold
                )
            },
            windowInsets = WindowInsets(0, 0, 0, 0),
            navigationIcon = {
                IconButton(onClick = {
                    navController.popBackIfCan()
                }) {
                    Icon(
                        Icons.Default.ArrowBackIosNew, contentDescription = "Back",
                        modifier = Modifier.size(20.dp)
                    )
                }
            },
            actions = {
                IconButton(onClick = {openGoogleMap(context, jobAddress)}) {
                    Icon(Icons.Default.Map,
                        contentDescription = "Go to Map",
                        modifier = Modifier.size(20.dp))
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
                            ClientCard(user = section.user, onAddressClick = {openGoogleMap(context, section.user.location)})
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

                    is HealthcareJobSection.JobWorkflow -> {
                        item {
                            section.serviceData.forEach { item ->

                                HealthcareServiceItem(item.first, item.second)

                                Spacer(Modifier.height(12.dp))
                            }

                            Spacer(Modifier.height(12.dp))
                        }
                    }

                    is HealthcareJobSection.ActionButtons -> {
                        item {
                            if (!isOnlyWatch) {
                                SlideToConfirmButton(
                                    isConfirmed = confirmed,
                                    onValueChange = {
                                        confirmed = it

                                        Log.d(tag, "HealthcareDetailScreen: $confirmed")
                                        viewModel.applyToJob(healthcareUid)
                                    }
                                )
                            }
                            Spacer(Modifier.height(24.dp))
                        }
                    }
                }
            }
        }
    }

}
