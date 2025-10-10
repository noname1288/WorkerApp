package com.example.workerapp.presentation.screens.detail.maintenance

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.workerapp.R
import com.example.workerapp.data.source.model.base.UserModel
import com.example.workerapp.data.source.model.cleaning.CleaningJobModel1
import com.example.workerapp.data.source.model.cleaning.CleaningServiceModel
import com.example.workerapp.data.source.model.healthcare.HealthcareJobModel
import com.example.workerapp.data.source.model.healthcare.HealthcareServiceModel
import com.example.workerapp.data.source.model.maintenance.MaintenanceJobModel
import com.example.workerapp.presentation.screens.detail.cleaning.CleaningJobSection
import com.example.workerapp.presentation.screens.detail.components.HealthcareServiceItem
import com.example.workerapp.presentation.screens.detail.healcare.HealthcareJobSection
import com.example.workerapp.presentation.screens.detail.healcare.HealthcareUiState
import com.example.workerapp.ui.detail.components.ClientCard
import com.example.workerapp.ui.detail.components.JobDetailCard
import com.example.workerapp.ui.detail.components.WeeklySchedule
import com.example.workerapp.utils.button.SlideToConfirmButton
import com.example.workerapp.utils.components.CircleLoadingIndicator
import com.example.workerapp.utils.ext.openGoogleMap
import com.example.workerapp.utils.ext.popBackIfCan

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MaintenanceDetailScreen(
    modifier: Modifier = Modifier,
    maintenanceUid: String,
    isOnlyWatch: Boolean = false,
    navController: NavController,
    viewModel: MaintenanceViewModel
) {
    val context = LocalContext.current
    val tag = "MaintenanceDetailScreen"

    val uiState by viewModel.uiState.collectAsState()
    var confirmed by rememberSaveable { mutableStateOf(false) }
    var jobAddress by rememberSaveable { mutableStateOf("") }

    var sections = listOf<MaintenanceJobSection>()

    LaunchedEffect(Unit) {
        viewModel.fetchJobDetail(maintenanceUid)
    }

    when(uiState){
        is MaintenanceDetailUIState.Error -> {
            Toast.makeText(context, (uiState as HealthcareUiState.Error).message, Toast.LENGTH_LONG)
                .show()
        }
        MaintenanceDetailUIState.Idle -> {}
        MaintenanceDetailUIState.Loading -> {
            CircleLoadingIndicator()
        }
        is MaintenanceDetailUIState.Success -> {
            val job = (uiState as MaintenanceDetailUIState.Success).maintenanceJob
            jobAddress = job.location

            sections = listOf(
                MaintenanceJobSection.UserInfo(job.user),
                MaintenanceJobSection.JobDetails(job),
                MaintenanceJobSection.WeeklySchedule(
                    job.listDays,
                    job.listDays.size != 1
                ),
                MaintenanceJobSection.JobWorkflow,
                MaintenanceJobSection.ActionButtons
            )


        }
    }

    Column(Modifier.fillMaxSize()) {
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
                IconButton(onClick = { openGoogleMap(context, jobAddress) }) {
                    Icon(
                        Icons.Default.Map,
                        contentDescription = "Go to Map",
                        modifier = Modifier.size(20.dp)
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
        )

        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            item {
                Spacer(Modifier.height(16.dp))
            }

            sections.forEach { section ->
                when (section) {
                    is MaintenanceJobSection.UserInfo -> {
                        item {
                            ClientCard(
                                user = section.user,
                                onAddressClick = { openGoogleMap(context, section.user.location) })
                            Spacer(Modifier.height(12.dp))
                        }
                    }

                    is MaintenanceJobSection.JobDetails -> {
                        item {
                            JobDetailCard(section.job)
                            Spacer(Modifier.height(12.dp))
                        }
                    }

                    is MaintenanceJobSection.WeeklySchedule -> {
                        item {
                            WeeklySchedule(section.days)
                            Spacer(Modifier.height(12.dp))
                        }
                    }

                    is MaintenanceJobSection.JobWorkflow -> {

                    }

                    is MaintenanceJobSection.ActionButtons -> {}
                }
            }
        }
    }
}

sealed class MaintenanceJobSection {
    data class UserInfo(val user: UserModel) : MaintenanceJobSection()
    data class JobDetails(val job: MaintenanceJobModel) : MaintenanceJobSection()
    data class WeeklySchedule(val days: List<String>, val isWeekly: Boolean) :
        MaintenanceJobSection()

    object JobWorkflow : MaintenanceJobSection()

    object ActionButtons : MaintenanceJobSection()
}

@Preview
@Composable
fun PrevMainFlow(modifier: Modifier = Modifier) {

}