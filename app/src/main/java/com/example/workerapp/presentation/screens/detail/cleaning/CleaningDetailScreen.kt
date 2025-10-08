package com.example.workerapp.presentation.screens.detail.cleaning

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.workerapp.R
import com.example.workerapp.data.source.model.base.UserModel
import com.example.workerapp.data.source.model.cleaning.CleaningJobModel1
import com.example.workerapp.data.source.model.cleaning.CleaningServiceModel
import com.example.workerapp.presentation.screens.detail.cleaning.CleaningJobSection.JobDetails
import com.example.workerapp.presentation.screens.detail.cleaning.CleaningJobSection.UserInfo
import com.example.workerapp.ui.detail.cleaning.CleaningUiState
import com.example.workerapp.ui.detail.cleaning.CleaningViewModel
import com.example.workerapp.ui.detail.components.ClientCard
import com.example.workerapp.ui.detail.components.JobDetailCard
import com.example.workerapp.ui.detail.components.JobWorkflow
import com.example.workerapp.ui.detail.components.WeeklySchedule
import com.example.workerapp.utils.button.SlideToConfirmButton
import com.example.workerapp.utils.components.CircleLoadingIndicator
import com.example.workerapp.utils.ext.openGoogleMap
import com.example.workerapp.utils.ext.popBackIfCan

sealed class CleaningJobSection {
    data class UserInfo(val user: UserModel) : CleaningJobSection()
    data class JobDetails(val job: CleaningJobModel1) : CleaningJobSection()
    data class WeeklySchedule(val days: List<String>, val isWeekly: Boolean) : CleaningJobSection()
    data class AdditionalJob(val isCooking: Boolean, val isIroning: Boolean) : CleaningJobSection()

    data class JobWorkflow(val services: List<CleaningServiceModel>) : CleaningJobSection()
    object ActionButtons : CleaningJobSection()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CleaningDetailScreen(
    modifier: Modifier = Modifier,
    cleaningUid: String,
    isOnlyWatch: Boolean = false,
    viewModel: CleaningViewModel, navController: NavController
) {
    val tag = "CleaningDetailScreen"
    val context = LocalContext.current

    var sections = emptyList<CleaningJobSection>()

    val uiState by viewModel.uiState.collectAsState()
    val applyState by viewModel.applyState.collectAsState()
    var confirmed by rememberSaveable { mutableStateOf(false) }
    var jobAddress by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(applyState) {
        when (applyState) {
            true -> {
                Toast.makeText(context, "Ứng tuyển thành công!", Toast.LENGTH_LONG).show()
//                navController.popBackIfCan()
            }

            false -> {
                viewModel.updateApplyState(null)
                confirmed = false
            }

            else -> {
                viewModel.fetchJobDetail(cleaningUid)
            }
        }
    }

    when (uiState) {
        is CleaningUiState.Success -> {
            val jobDetail = (uiState as CleaningUiState.Success).job
            val services = (uiState as CleaningUiState.Success).services
            jobAddress = jobDetail.location
            Log.d(tag, "CleaningDetailScreen: Fetched job detail: $jobDetail")

            sections = listOf(
                UserInfo(jobDetail.user),
                JobDetails(jobDetail),
                CleaningJobSection.WeeklySchedule(
                    jobDetail.listDays,
                    jobDetail.listDays.size > 1
                ),
                CleaningJobSection.AdditionalJob(jobDetail.isCooking, jobDetail.isIroning),
                CleaningJobSection.JobWorkflow(services),
                CleaningJobSection.ActionButtons
            )
        }

        is CleaningUiState.Error -> {
            Toast.makeText(
                context,
                (uiState as CleaningUiState.Error).message,
                Toast.LENGTH_LONG
            ).show()
        }

        CleaningUiState.Loading -> {
            Box(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircleLoadingIndicator()
            }
        }

        CleaningUiState.Idle -> {}
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
                        Icons.Default.ArrowBackIosNew,
                        contentDescription = "Back",
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
                    is UserInfo -> {
                        item {
                            ClientCard(user = section.user, onAddressClick = {openGoogleMap(context, section.user.location)})
                            Spacer(Modifier.height(12.dp))
                        }
                    }

                    is JobDetails -> {
                        item {
                            JobDetailCard(job = section.job)
                            Spacer(Modifier.height(12.dp))
                        }
                    }

                    is CleaningJobSection.WeeklySchedule -> {
                        item {
                            WeeklySchedule(section.days, section.isWeekly)
                            Spacer(Modifier.height(12.dp))
                        }
                    }

                    is CleaningJobSection.AdditionalJob -> {
                        item {
                            AdditionalJob(section.isCooking, section.isIroning)
                            Spacer(Modifier.height(24.dp))
                        }
                    }

                    is CleaningJobSection.JobWorkflow -> {
                        item {
                            JobWorkflow(section.services)
                            Spacer(Modifier.height(24.dp))
                        }
                    }

                    is CleaningJobSection.ActionButtons -> {
                        item {
                            if (!isOnlyWatch) {
                                SlideToConfirmButton(
                                    isConfirmed = confirmed,
                                    onValueChange = {
                                        confirmed = it

                                        Log.d(tag, "CleaningDetailScreen: Confirmed")
                                        viewModel.applyToJob(cleaningUid)
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

}

@Composable
fun AdditionalJob(isCooking: Boolean = true, isIroning: Boolean = true) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
    ) {
        if (isCooking) {
            AdditionalJobItem(
                Modifier.weight(1f),
                icon = R.drawable.ic_cooking,
                title = "Nấu ăn: 1 giờ"
            )
        }

        if (isIroning) {
            AdditionalJobItem(
                Modifier.weight(1f),
                icon = R.drawable.ic_iron,
                title = "Ủi đồ: 1 giờ"
            )
        }
    }
}

@Composable
fun AdditionalJobItem(modifier: Modifier = Modifier, icon: Int, title: String) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        border = BorderStroke(1.dp, colorResource(R.color.light_gray)),
        elevation = CardDefaults.cardElevation(3.dp),
        modifier = modifier

    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painterResource(icon),
                null,
                modifier = Modifier.size(48.dp)
            )
            Spacer(Modifier.height(12.dp))
            Text(
                title, fontSize = 12.sp,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold,
                )
            )
        }
    }
}
