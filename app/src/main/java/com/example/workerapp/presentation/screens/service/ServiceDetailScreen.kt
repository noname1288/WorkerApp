package com.example.workerapp.presentation.screens.service

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.workerapp.data.source.model.base.JobModel1
import com.example.workerapp.data.source.model.cleaning.CleaningJobModel1
import com.example.workerapp.data.source.model.healthcare.HealthcareJobModel
import com.example.workerapp.navigation.AppRoutes
import com.example.workerapp.ui.home.components.CleaningJobCard
import com.example.workerapp.ui.home.components.HealthcareJobCard
import com.example.workerapp.utils.ServiceType
import com.example.workerapp.utils.components.CircleLoadingIndicator
import com.example.workerapp.utils.ext.navigateWithArgs
import com.example.workerapp.utils.ext.popBackIfCan

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceDetailScreen(
    modifier: Modifier = Modifier,
    serviceType: String,
    viewModel: ServiceViewModel,
    navController: NavController
) {
    val context = LocalContext.current

    val serviceTypeText = when (serviceType) {
        ServiceType.CleaningType -> "Dọn dẹp"
        ServiceType.HealthcareType -> "Chăm sóc sức khỏe"
        ServiceType.MaintenanceType -> "Bảo trì"
        else -> "Dịch vụ"
    }

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.updateServiceType(serviceType)
        viewModel.fetchData()
    }

    Column(modifier.fillMaxSize()) {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    serviceTypeText,
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
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
        )

        Spacer(Modifier.height(12.dp))

        when (uiState) {
            is ServiceUIState.Error -> {
                LaunchedEffect(uiState) {
                    Toast.makeText(
                        context,
                        (uiState as ServiceUIState.Error).message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            ServiceUIState.Idle -> {}

            ServiceUIState.Loading -> {
                CircleLoadingIndicator()
            }

            is ServiceUIState.Success -> {
                val jobs = (uiState as ServiceUIState.Success).jobs

                LazyColumn(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (jobs.isEmpty()) {
                        item {
                            Box(
                                Modifier
                                    .height(300.dp)
                                    .fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) { Text("Không có công việc nào") }
                        }
                    } else {
                        itemsIndexed(jobs) { index, job ->
                            when (serviceType) {
                                ServiceType.CleaningType -> {
                                    CleaningJobCard(
                                        job as CleaningJobModel1,
                                        onClick = {
                                            navController.navigateWithArgs(
                                                route = AppRoutes.CLEANING_DETAIL,
                                                args = arrayOf(job.uid, false)
                                            )
                                        })
                                }

                                ServiceType.HealthcareType -> {
                                    HealthcareJobCard(
                                        job as HealthcareJobModel,
                                        onClick = {
                                            navController.navigateWithArgs(
                                                route = AppRoutes.HEALTHCARE_DETAIL,
                                                args = arrayOf(job.uid, false)
                                            )
                                        })
                                }

                                else -> {
                                    Text("Loại dịch vụ không hợp lệ")
                                }
                            }
                        }

                        item { Spacer(Modifier.height(24.dp)) }
                    }
                }
            }
        }


    }
}