package com.example.workerapp.presentation.screens.review

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.KeyboardArrowDown
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.workerapp.R
import com.example.workerapp.data.source.model.ReviewModel
import com.example.workerapp.data.source.remote.dto.wrapper.ReviewWrapperLayer
import com.example.workerapp.utils.components.CircleLoadingIndicator
import com.example.workerapp.utils.ext.popBackIfCan

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: ReviewViewModel
) {
    val tag = "ReviewScreen"
    val context = LocalContext.current

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchReviews()
    }

    Column(Modifier.fillMaxSize()) {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    stringResource(R.string.review_title),
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

            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
        )

        when (uiState) {
            is ReviewUiState.Error -> {
                Toast.makeText(
                    context,
                    (uiState as ReviewUiState.Error).message,
                    Toast.LENGTH_SHORT
                ).show()
            }

            ReviewUiState.Idle -> {}
            ReviewUiState.Loading -> {
                Box(
                    Modifier
                        .fillMaxSize()
                        .background(Color.LightGray.copy(alpha = 0.5f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircleLoadingIndicator()
                }
            }

            is ReviewUiState.Success -> {
                val reviews = (uiState as ReviewUiState.Success).reviews
                val cleaningReviews = reviews.CLEANING
                val healthcareReviews = reviews.HEALTHCARE
                val maintenanceReviews = reviews.MAINTENANCE

                LazyColumn {
                    item {
                        Spacer(Modifier.height(16.dp))
                    }

                    item {
                        if (cleaningReviews == null && healthcareReviews == null && maintenanceReviews == null) {
                            Box(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    "Chưa có đánh giá nào",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = colorResource(R.color.subtext)
                                    )
                                )
                            }
                        }
                    }

                    cleaningReviews?.let {
                        item {
                            ReviewHeaderRow(wrapper = it, title = "Đánh giá dịch vụ dọn dẹp")

                            Spacer(Modifier.height(8.dp))

                            val reviews = it.reviews

                            if (reviews.isEmpty()) {
                                Box(
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        "Chưa có đánh giá về dịch vụ này",
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            color = colorResource(R.color.subtext)
                                        )
                                    )
                                }
                            } else {
                                Column(
                                    verticalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.padding(bottom = 16.dp)
                                ) {
                                    reviews.forEach { item ->
                                        ReviewItemRow(item = item)
                                        HorizontalDivider()
                                    }
                                }
                            }
                        }
                    }

                    item {
                        Spacer(Modifier.height(16.dp))
                    }

                    healthcareReviews?.let {
                        item {
                            ReviewHeaderRow(wrapper = it, title = "Đánh giá dịch vụ chăm sóc sức khỏe")

                            Spacer(Modifier.height(8.dp))

                            val reviews = it.reviews

                            if (reviews.isEmpty()) {
                                Box(
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        "Chưa có đánh giá về dịch vụ này",
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            color = colorResource(R.color.subtext)
                                        )
                                    )
                                }
                            } else {
                                Column(
                                    verticalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.padding(bottom = 16.dp)
                                ) {
                                    reviews.forEach { item ->
                                        ReviewItemRow(item = item)
                                        HorizontalDivider()
                                    }
                                }
                            }
                        }
                    }

                    item {
                        Spacer(Modifier.height(16.dp))
                    }

                    maintenanceReviews?.let {
                        item {
                            ReviewHeaderRow(wrapper = it, title = "Đánh giá dịch vụ bảo trì")

                            Spacer(Modifier.height(8.dp))

                            val reviews = it.reviews

                            if (reviews.isEmpty()) {
                                Box(
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        "Chưa có đánh giá về dịch vụ này",
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            color = colorResource(R.color.subtext)
                                        )
                                    )
                                }
                            } else {
                                Column(
                                    verticalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.padding(bottom = 16.dp)
                                ) {
                                    reviews.forEach { item ->
                                        ReviewItemRow(item = item)
                                        HorizontalDivider()
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }


    }
}

@Composable
fun ReviewItemRow(modifier: Modifier = Modifier, item: ReviewModel) {
    val user = item.user

    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = user.avatar,
                error = painterResource(R.drawable.ic_launcher_background),
                contentDescription = "User's avatar who commented",
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(Modifier.width(16.dp))

            Text(
                text = user.username,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )
        }

        Spacer(Modifier.height(4.dp))

        RatingBar(item.rating)

        Spacer(Modifier.height(4.dp))

        Text(
            text = item.comment,
            style =
                MaterialTheme.typography.bodyMedium.copy(
                    color = colorResource(R.color.subtext)
                )
        )
    }
}

@Composable
fun RatingBar(rating: Double) {
    val fullStars = rating.toInt()
    val maxStars = 5

    Row() {
        repeat(fullStars) {
            Icon(
                Icons.Default.Star,
                contentDescription = null,
                tint = colorResource(R.color.light_orange_icon),
                modifier = Modifier.size(16.dp)
            )
        }
        repeat(maxStars - fullStars) {
            Icon(
                Icons.Default.Star,
                contentDescription = null,
                tint = Color.LightGray,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
fun ReviewHeaderRow(modifier: Modifier = Modifier, wrapper: ReviewWrapperLayer, title: String) {
    Row(
        Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(Modifier.weight(1f)) {
            Text(
                wrapper.rating.toString(),
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(Modifier.width(4.dp))

            Icon(
                Icons.Default.Star,
                contentDescription = null,
                tint = colorResource(R.color.light_orange_icon),
                modifier = Modifier.size(20.dp)
            )

            Spacer(Modifier.width(4.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 2
            )
        }

        Spacer(Modifier.width(8.dp))

        Icon(Icons.Outlined.KeyboardArrowDown, contentDescription = null)
    }
}