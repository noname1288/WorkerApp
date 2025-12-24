package com.example.workerapp.presentation.screens.detail_job.healthcare

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
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.workerapp.R
import com.example.workerapp.data.source.model.base.UserModel
import com.example.workerapp.data.source.model.healthcare.HealthcareJobModel
import com.example.workerapp.data.source.model.healthcare.HealthcareServiceModel
import com.example.workerapp.presentation.screens.detail_job.components.HealthcareServiceItem
import com.example.workerapp.presentation.screens.detail_job.healthcare.ApplyJobState
import com.example.workerapp.presentation.screens.detail_job.healthcare.CancelJobState
import com.example.workerapp.ui.detail.components.ClientCard
import com.example.workerapp.ui.detail.components.JobDetailCard
import com.example.workerapp.ui.detail.components.WeeklySchedule
import com.example.workerapp.utils.components.ApplyJobButton
import com.example.workerapp.utils.components.CancelJobButton
import com.example.workerapp.utils.components.CircleLoadingIndicator
import com.example.workerapp.utils.components.CommonAlertDialog
import com.example.workerapp.utils.components.ErrorDialog
import com.example.workerapp.utils.components.LoadingDialog
import com.example.workerapp.utils.components.SuccessDialog
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
    val tag = "HealthcareDetailScreen"

    val uiState by viewModel.uiState.collectAsState()
    val appJobState by viewModel.appJobState.collectAsState()
    val appliedState by viewModel.appliedState.collectAsState()
    val cancelState by viewModel.cancelJobState.collectAsState()

    var showApplyDialog by rememberSaveable { mutableStateOf(false) }
    var showAlertDialog by rememberSaveable { mutableStateOf(false) }
    var showCancelDialog by rememberSaveable { mutableStateOf(false) }
    var jobAddress by rememberSaveable { mutableStateOf("") }

    //init
    LaunchedEffect(Unit) {
        viewModel.fetchJobDetail(healthcareUid)
        viewModel.checkIfApplied(healthcareUid)
    }

    val job = uiState.job
    val serviceData = uiState.serviceData
    val isLoading = uiState.isLoading

    val sections = remember(job, serviceData) {
        if (job != null) {
            jobAddress = job.location
            listOf(
                HealthcareJobSection.UserInfo(job.user),
                HealthcareJobSection.JobDetails(job),
                HealthcareJobSection.WeeklySchedule(job.listDays, job.listDays.size > 1),
                HealthcareJobSection.JobWorkflow(serviceData),
                HealthcareJobSection.ActionButtons
            )
        } else emptyList()
    }

    Column(modifier.fillMaxSize()) {
        CenterAlignedTopAppBar(
            title = {
                Text(stringResource(R.string.job_detail_title), fontWeight = FontWeight.Bold)
            },
            windowInsets = WindowInsets(0, 0, 0, 0),
            navigationIcon = {
                IconButton(onClick = { navController.popBackIfCan() }) {
                    Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back", modifier = Modifier.size(20.dp))
                }
            },
            actions = {
                IconButton(onClick = { openGoogleMap(context, jobAddress) }) {
                    Icon(Icons.Default.Map, contentDescription = "Go to Map", modifier = Modifier.size(20.dp))
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
        )

        when {
            isLoading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircleLoadingIndicator()
                }
            }

            job == null -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Không tìm thấy công việc", color = colorResource(R.color.subtext))
                }
            }

            else -> {
                LazyColumn(
                    Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                ) {
                    item { Spacer(Modifier.height(16.dp)) }

                    sections.forEach { section ->
                        when (section) {
                            is HealthcareJobSection.UserInfo -> {
                                item {
                                    ClientCard(
                                        user = section.user,
                                        onAddressClick = { openGoogleMap(context, section.user.location) }
                                    )
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
                                    section.serviceData.forEach { pair ->
                                        HealthcareServiceItem(pair.first, pair.second)
                                        Spacer(Modifier.height(12.dp))
                                    }
                                    Spacer(Modifier.height(12.dp))
                                }
                            }

                            is HealthcareJobSection.ActionButtons -> {
                                item {
                                    if (!isOnlyWatch) {
                                        when (appliedState) {
                                            true -> {
                                                CancelJobButton(
                                                    onCancel = {
                                                        showAlertDialog = true
                                                    }
                                                )
                                            }

                                            false -> {
                                                ApplyJobButton(onConfirm = {
                                                    showApplyDialog = true
//                                                    viewModel.applyToJob(healthcareUid)
                                                })
                                            }

                                            null -> {}
                                        }
                                    }
                                    Spacer(Modifier.height(24.dp))
                                }
                            }
                        }
                    }

                    item {
                        if (showApplyDialog) {
                            when (appJobState) {
                                is ApplyJobState.Error -> {
                                    val message = (appJobState as ApplyJobState.Error).message
                                    ErrorDialog(
                                        content = message,
                                        onDismiss = {
                                            showApplyDialog = false
                                        }
                                    )
                                }

                                ApplyJobState.Idle -> {}
                                ApplyJobState.Loading -> LoadingDialog()
                                ApplyJobState.Success -> {
                                    SuccessDialog(
                                        content = "Ứng tuyển thành công!",
                                        onDismiss = { showApplyDialog = false })
                                }
                            }
                        }

                        if (showAlertDialog){
                            CommonAlertDialog(
                                content = "Bạn có chắc chắn muốn hủy ứng tuyển công việc này?",
                                onDismiss = {
                                    showAlertDialog = false
                                },
                                onConfirm = {
                                    // Confirm cancel apply
                                    showAlertDialog = false
                                    viewModel.cancelApplication()
                                    showCancelDialog = true
                                }
                            )
                        }

                        if (showCancelDialog){
                            when(cancelState){
                                is CancelJobState.Error -> {
                                    val message = (cancelState as CancelJobState.Error).message
                                    ErrorDialog(
                                        content = message,
                                        onDismiss = {
                                            showCancelDialog = false
                                        }
                                    )
                                }
                                CancelJobState.Idle -> {}
                                CancelJobState.Loading -> LoadingDialog()
                                CancelJobState.Success -> {
                                    SuccessDialog(
                                        content = "Huỷ thành công!",
                                        onDismiss = { showCancelDialog = false })
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
