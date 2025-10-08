package com.example.workerapp.presentation.screens.home

import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PowerSettingsNew
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.workerapp.R
import com.example.workerapp.navigation.AppRoutes
import com.example.workerapp.ui.home.HomeUiState
import com.example.workerapp.ui.home.HomeViewModel
import com.example.workerapp.utils.ServiceType
import com.example.workerapp.utils.cached.UserSession
import com.example.workerapp.utils.components.CircleLoadingIndicator
import com.example.workerapp.utils.ext.navigateWithArgs

/**
 * Sealed class representing different sections of the Home screen
 */
sealed class HomeSection {
    object Avatar : HomeSection()
    object Category : HomeSection()
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: HomeViewModel
) {
    val context = LocalContext.current

    val uiState by viewModel.homeUiState.collectAsState()
    var shouldAskPermission by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        viewModel.fetchServices()
    }

    LaunchedEffect(Unit) {
        Log.d("HomeScreen", "$shouldAskPermission")
    }

    when (uiState) {
        is HomeUiState.Error -> {
            Toast.makeText(context, (uiState as HomeUiState.Error).message, Toast.LENGTH_LONG).show()
        }

        HomeUiState.Idle -> {}

        HomeUiState.Loading -> {
            CircleLoadingIndicator()
        }

        is HomeUiState.Success -> {}
    }

    // Create list of sections to display
    val homeSections = listOf(
        HomeSection.Avatar,
        HomeSection.Category,
    )

    Box(Modifier.fillMaxSize()) {
        // Background gradient
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(270.dp) // chiều cao phần nền cam
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFFF8000), // cam đậm
                            Color(0xFFFFA726)  // cam nhạt
                        )
                    ),
                )
        )

        LazyColumn(
            modifier = modifier
                .fillMaxSize()
        ) {
            item {
                Spacer(Modifier.height(16.dp))
            }

            homeSections.forEach { section ->
                item {
                    when (section) {
                        is HomeSection.Avatar -> {
                            CustomAvatarRow()
                            Spacer(Modifier.height(24.dp))
                        }

                        is HomeSection.Category -> {
                            Column(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp)
                            ) {
                                Text(
                                    "Danh mục công việc",
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = colorResource(R.color.white)
                                    )
                                )

                                Spacer(Modifier.height(12.dp))

                                CategoryCard(
                                    label = stringResource(R.string.cleaning_service_title),
                                    body = stringResource(R.string.cleaning_service_body),
                                    imageInt = R.drawable.img_housekeeping,
                                    onClick = {
                                        navController.navigateWithArgs(
                                            AppRoutes.SERVICE_DETAIL,
                                            args = arrayOf(ServiceType.CleaningType)
                                        )
                                    }
                                )
                                Spacer(Modifier.height(12.dp))

                                CategoryCard(
                                    label = stringResource(R.string.healthcare_service_title),
                                    body = stringResource(R.string.healthcare_service_body),
                                    imageInt = R.drawable.img_healthcare_1,
                                    isReverse = false,
                                    onClick = {
                                        navController.navigateWithArgs(
                                            AppRoutes.SERVICE_DETAIL,
                                            args = arrayOf(ServiceType.HealthcareType)
                                        )
                                    }
                                )
                                Spacer(Modifier.height(12.dp))

                                CategoryCard(
                                    label = stringResource(R.string.maintenance_service_title),
                                    body = stringResource(R.string.maintenance_service_body),
                                    imageInt = R.drawable.img_maintenance,
                                    onClick = {
                                        navController.navigateWithArgs(
                                            AppRoutes.SERVICE_DETAIL,
                                            args = arrayOf(ServiceType.MaintenanceType)
                                        )
                                    }
                                )
                            }
                            Spacer(Modifier.height(12.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryCard(
    modifier: Modifier = Modifier,
    label: String = "Cleaning",
    body: String = "Dọn dẹp nhà cửa",
    imageInt: Int = R.drawable.ic_launcher_background,
    isReverse: Boolean = true,
    onClick: () -> Unit,
) {
    Card(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
    ) {
        Column(
            Modifier.padding(16.dp),
            horizontalAlignment = Alignment.End
        ) {
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isReverse) {
                    Column(Modifier.weight(1f)) {
                        Text(
                            label,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = colorResource(R.color.orange_primary),
                                textAlign = TextAlign.Start
                            ), maxLines = 2,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Text(
                            text = body,
                            style = MaterialTheme.typography.bodyMedium.copy()
                        )
                    }
                    Spacer(Modifier.width(16.dp))
                    Image(
                        painterResource(imageInt),
                        null,
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier
                            .size(100.dp)
                    )
                } else {
                    Image(
                        painterResource(imageInt),
                        null,
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier
                            .size(100.dp)
                    )

                    Spacer(Modifier.width(16.dp))

                    Column(Modifier.weight(1f)) {
                        Text(
                            label,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.End,
                                color = colorResource(R.color.orange_primary)
                            ), maxLines = 2,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Text(
                            text = body,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                textAlign = TextAlign.End
                            )
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            Image(
                painterResource(R.drawable.img_next),
                null,
                modifier = Modifier.size(36.dp)
            )
        }

    }
}

@Composable
fun CustomAvatarRow(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = UserSession.userProfilePicUrl, // Thay URL này bằng URL ảnh của bạn
            contentDescription = null,
            error = painterResource(R.drawable.ic_launcher_background),
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        Spacer(Modifier.width(8.dp))

        Text(
            UserSession.displayName ?: "Khách",
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Bold,
                color = Color.White
            ),
            maxLines = 1,
            modifier = Modifier.widthIn(max = 120.dp)
        )

        Spacer(Modifier.weight(1f))

        Button(
            onClick = { onClick() },
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(R.color.gray).copy(alpha = 0.5f)
            )
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Rounded.PowerSettingsNew, null, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(4.dp))
                Text(
                    "Bật/Tắt",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold,
                    )
                )
            }
        }

    }
}

