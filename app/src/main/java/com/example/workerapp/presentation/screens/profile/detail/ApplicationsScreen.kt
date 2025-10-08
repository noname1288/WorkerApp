package com.example.workerapp.presentation.screens.profile.detail

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
import androidx.compose.material3.Card
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.workerapp.R
import com.example.workerapp.data.source.remote.dto.ApplicationWrapper
import com.example.workerapp.presentation.screens.profile.ApplicationsUiState
import com.example.workerapp.presentation.screens.profile.ProfileViewModel
import com.example.workerapp.utils.components.CircleLoadingIndicator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApplicationsScreen(modifier: Modifier = Modifier, viewModel: ProfileViewModel) {
    val context = LocalContext.current

    val uiState by viewModel.applicationsState.collectAsState()
    var applicationList by rememberSaveable { mutableStateOf(listOf<ApplicationWrapper>()) }

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
            CircleLoadingIndicator()
        }

        is ApplicationsUiState.Success -> {
            applicationList = (uiState as ApplicationsUiState.Success).data
        }
    }

    LazyColumn(Modifier.fillMaxSize()) {
        item {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        stringResource(R.string.list_applications_title),
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

            Spacer(Modifier.height(16.dp))
        }

        item {
            if (applicationList.isEmpty()) {
                Text("Không tìm thấy đơn ứng tuyển nào")
            } else {
                applicationList.forEach { application ->
                    ApplicationItem(applicationWrapper = application)
                    Spacer(Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
fun ApplicationItem(applicationWrapper: ApplicationWrapper) {
    Card(
        Modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(
                applicationWrapper.uid, style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}