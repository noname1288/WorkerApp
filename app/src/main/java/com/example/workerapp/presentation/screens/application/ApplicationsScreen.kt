package com.example.workerapp.presentation.screens.application

import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.workerapp.R
import com.example.workerapp.data.source.remote.dto.response.ApplicationDto
import com.example.workerapp.presentation.screens.profile.ApplicationsUiState
import com.example.workerapp.utils.ServiceType
import com.example.workerapp.utils.components.CircleLoadingIndicator
import com.example.workerapp.utils.components.CustomChip
import com.example.workerapp.utils.ext.popBackIfCan

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApplicationsScreen(
    modifier: Modifier = Modifier,
    viewModel: ApplicationViewModel,
    navcontroler: NavController
) {
    val context = LocalContext.current

    val uiState by viewModel.applicationsState.collectAsState()
    var applicationList by rememberSaveable { mutableStateOf(listOf<ApplicationDto>()) }

    LaunchedEffect(Unit) {
        viewModel.fetchApplications()
    }

    when (uiState) {
        is ApplicationsUiState.Error -> {
            Toast.makeText(
                context,
                (uiState as ApplicationsUiState.Error).message,
                Toast.LENGTH_LONG
            ).show()
        }

        ApplicationsUiState.Idle -> {}
        ApplicationsUiState.Loading -> {
            Box(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircleLoadingIndicator()
            }
        }

        is ApplicationsUiState.Success -> {
            applicationList = (uiState as ApplicationsUiState.Success).data
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    stringResource(R.string.list_applications_title),
                    fontWeight = FontWeight.Bold
                )
            },
            windowInsets = WindowInsets(0, 0, 0, 0),
            navigationIcon = {
                IconButton(onClick = {
                    navcontroler.popBackIfCan()
                }) {
                    Icon(
                        Icons.Default.ArrowBackIosNew, contentDescription = "Back",
                        modifier = Modifier.size(20.dp)
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
        )

        Spacer(Modifier.height(16.dp))

        if (applicationList.size == 0) {
            Box (Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center){
                Text(
                    text = "Bạn chưa ứng tuyển công việc nào.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .padding(16.dp),
                )
            }
        } else {
            LazyColumn() {
                items(applicationList.size) { index ->
                    ApplicationItemCard(item = applicationList[index])
                    HorizontalDivider()
                }
            }
        }
    }
}

@Composable
fun ApplicationItemCard(item: ApplicationDto) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        Column() {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = ServiceType.translateToVietnamese(item.serviceType),
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                    maxLines = 2,
                    modifier = Modifier.weight(1f)
                )

                CustomChip(status = item.status)
            }

            Spacer(Modifier.height(4.dp))

            Text(
                "Địa chỉ: ${item.job.location}", style = MaterialTheme.typography.bodyMedium,
                maxLines = 2
            )

            Spacer(Modifier.height(2.dp))

            Text(
                "Khách hàng đánh giá: ${if (item.isReview) "Đã đánh giá" else "Chưa"}",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(Modifier.height(2.dp))

            Text(
                "Thời điểm ứng tuyển: ${item.createdAt}",
                style = MaterialTheme.typography.bodyMedium
            )

        }
    }
}
