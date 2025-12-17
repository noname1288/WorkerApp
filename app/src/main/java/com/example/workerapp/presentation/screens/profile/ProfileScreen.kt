package com.example.workerapp.presentation.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CrisisAlert
import androidx.compose.material.icons.outlined.DocumentScanner
import androidx.compose.material.icons.outlined.Domain
import androidx.compose.material.icons.outlined.RateReview
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.SupportAgent
import androidx.compose.material.icons.outlined.WorkHistory
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.workerapp.R
import com.example.workerapp.navigation.AppRoutes
import com.example.workerapp.presentation.screens.authen.AuthViewModel
import com.example.workerapp.presentation.screens.profile.components.CustomExtendedButton
import com.example.workerapp.utils.cached.UserSession
import com.example.workerapp.utils.ext.safeNavigate

sealed class ProfileSection {
    object Avatar : ProfileSection()
    object Settings : ProfileSection()
    object Logout : ProfileSection()
}

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: AuthViewModel,
    profileViewModel: ProfileViewModel,
) {

    val sections = listOf(
        ProfileSection.Avatar,
        ProfileSection.Settings,
        ProfileSection.Logout
    )

    var showLogoutDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier
            .fillMaxSize()
    ) {
        sections.forEach { section ->
            when (section) {
                is ProfileSection.Avatar -> {
                    item {
                        ProfileHeader(
                            onProfileClick = {
                                navController.safeNavigate(AppRoutes.PROFILE_DETAIL)
                            }
                        )
                        Spacer(Modifier.height(16.dp))
                    }
                }

                is ProfileSection.Settings -> {
                    item {
                        SettingButtons(
                            onApplicationsClick = {
                                navController.navigate(AppRoutes.LIST_APPLICATIONS)
                            },
                            onReviewClick = {
                                navController.navigate(AppRoutes.REVIEW_SCREEN)
                            },
                            onPasswordChangeClick = {
                                navController.navigate(AppRoutes.CHANGE_PASSWORD)
                            },
                            onTermClick = {
                                navController.navigate(AppRoutes.POLICY_SCREEN)
                            }
                        )
                        Spacer(Modifier.height(32.dp))
                    }
                }

                is ProfileSection.Logout -> {
                    item {
                        Button(
                            onClick = {

                                showLogoutDialog = true
                            },
                            shape = RoundedCornerShape(0.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colorResource(R.color.light_orange_icon)
                            ),
                            contentPadding = PaddingValues(vertical = 20.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(stringResource(R.string.logout_title), color = Color.White)
                        }
                        Spacer(Modifier.height(32.dp))
                    }
                }
            }
        }

        if (showLogoutDialog) {
            item {
                AlertDialog(
                    icon = {
                        Icon(Icons.Default.CrisisAlert, contentDescription = "Example Icon")
                    },
                    title = { Text("Thông báo") },
                    text = { Text("Bạn có chắc chắn muốn đăng xuất không?") },
                    onDismissRequest = { showLogoutDialog = false },
                    dismissButton = {
                        TextButton(
                            onClick = {
                                showLogoutDialog = false
                            }
                        ) {
                            Text("Huỷ")
                        }
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                showLogoutDialog = false

                                authViewModel.logout()

                                navController.safeNavigate(
                                    AppRoutes.LOGIN,
                                    popUpToRoute = AppRoutes.LOGIN,
                                    inclusive = false,
                                    restore = false
                                )
                            }
                        ) {
                            Text("Đăng xuất")
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun ProfileHeader(onProfileClick: () -> Unit = {}) {
    Row(
        Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 32.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        AsyncImage(
            model = UserSession.userProfilePicUrl,
            null,
            error = painterResource(R.drawable.ic_launcher_background),
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
        )

        Spacer(Modifier.width(16.dp))

        Column {
            Text(
                UserSession.displayName ?: "Khách",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                "Xem hồ sơ >",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = colorResource(R.color.green)
                ),
                modifier = Modifier.clickable {
                    onProfileClick()
                }
            )
        }
    }
}

@Composable
fun SettingButtons(
    modifier: Modifier = Modifier,
    onApplicationsClick: () -> Unit = {},
    onHistoryClick: () -> Unit = {},
    onReviewClick: () -> Unit = {},
    onTermClick: () -> Unit = {},
    onSupportClick: () -> Unit = {},
    onPasswordChangeClick: () -> Unit = {}
) {
    Column(modifier.fillMaxWidth()) {

        CustomExtendedButton(
            label = stringResource(R.string.applied_job),
            leadingIcon = Icons.Outlined.DocumentScanner,
            onClick = { onApplicationsClick() }
        )

        HorizontalDivider()

//        CustomExtendedButton(
//            label = stringResource(R.string.job_history_title),
//            leadingIcon = Icons.Outlined.WorkHistory,
//            onClick = { onHistoryClick() }
//        )
//
//        HorizontalDivider()

        CustomExtendedButton(
            label = stringResource(R.string.review_title_from_customer),
            leadingIcon = Icons.Outlined.RateReview,
            onClick = { onReviewClick() }
        )

        HorizontalDivider()

        CustomExtendedButton(
            label = stringResource(R.string.term_title),
            leadingIcon = Icons.Outlined.Domain,
            onClick = { onTermClick() }
        )

        HorizontalDivider()

//        CustomExtendedButton(
//            label = stringResource(R.string.support_title),
//            leadingIcon = Icons.Outlined.SupportAgent,
//            onClick = { onSupportClick() }
//        )
//
//        HorizontalDivider()

        CustomExtendedButton(
            label = stringResource(R.string.change_password_title),
            leadingIcon = Icons.Outlined.Settings,
            onClick = { onPasswordChangeClick() }
        )

    }
}
