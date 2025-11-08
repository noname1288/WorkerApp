package com.example.workerapp.presentation.screens.profile.detail

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import com.example.workerapp.data.source.model.MapResult
import com.example.workerapp.navigation.AppRoutes
import com.example.workerapp.presentation.screens.profile.components.GenderSelectionRow
import com.example.workerapp.presentation.screens.profile.components.NewTextFieldRow
import com.example.workerapp.utils.TimeUtils
import com.example.workerapp.utils.components.CircleLoadingIndicator
import com.example.workerapp.utils.components.DatePickerModal
import com.example.workerapp.utils.ext.popBackIfCan
import com.google.maps.android.compose.GoogleMap

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileDetailScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: ProfileDetailViewModel
) {
    val context = LocalContext.current

    val uiState by viewModel.profileDetailState.collectAsState()
    val user by viewModel.userFlow.collectAsState()
    val form by viewModel.form.collectAsState()

    var imageUri by remember { mutableStateOf<Uri?>(null) }

    var dropDownListexpanded by remember { mutableStateOf(false) }
    var showDateDialog by remember { mutableStateOf(false) }
    var showConfirmDialog by remember { mutableStateOf(false) }
    var genderList = listOf("Nam", "Nữ", "Khác")
    val selectedGender = form.gender

    val activeColor = Color.Black
    val borderColor = Color(0xFF7A7A7A)
    val iconColor = Color(0xFF616161)

    var showMap by rememberSaveable { mutableStateOf(false) }

    val pickMedia =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                imageUri = uri
                viewModel.onImagePicked(uri.toString())
            }
        }

    val resultFlow = navController.currentBackStackEntry
        ?.savedStateHandle
        ?.getStateFlow<MapResult?>("map_result", null)
    val mapResult by resultFlow?.collectAsState() ?: remember { mutableStateOf(null) }

    LaunchedEffect(mapResult) {
        mapResult?.let { res ->
            viewModel.onLocationPicked(res.address, res.lat, res.lng)

            navController.currentBackStackEntry?.savedStateHandle?.set("map_result", null)
        }
    }

    LaunchedEffect((uiState as? ProfileDetailUiState.Error)?.message) {
        (uiState as? ProfileDetailUiState.Error)?.message?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect((uiState as? ProfileDetailUiState.Success)?.message) {
        (uiState as? ProfileDetailUiState.Success)?.message?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        Modifier
            .fillMaxSize()
            .imePadding()
            .background(Color.White)
    ) {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    stringResource(R.string.edit_profile_title),
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

        Spacer(Modifier.height(16.dp))

        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            item {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Box {
                        AsyncImage(
                            model = imageUri ?: form.imagePath ?: user?.avatar,
                            error = painterResource(R.drawable.ic_launcher_background),
                            contentDescription = null,
                            modifier = Modifier
                                .size(140.dp)
                                .padding(8.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )

                        IconButton(
                            onClick = {
                                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                            },
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .clip(CircleShape)
                                .background(colorResource(R.color.orange_text))
                                .border(3.dp, Color.White, CircleShape),
                        ) {
                            Icon(Icons.Default.PhotoCamera, null, tint = Color.White)
                        }
                    }
                }

                Spacer(Modifier.height(12.dp))
            }

            item {

                NewTextFieldRow(
                    value = form.username,
                    onValueChange = viewModel::onUsernameChange,
                    title = "Tên đầy đủ",
                    placeholderText = "Nguyễn Văn A...",
                )

                Spacer(Modifier.height(16.dp))

                GenderSelectionRow(
                    title = "Giới tính",
                    genderList = genderList,
                    selectedGender = selectedGender,
                    onGenderSelected = { gender ->
                        viewModel.onGenderChange(gender)
                    }
                )

                Spacer(Modifier.height(16.dp))

                NewTextFieldRow(
                    value = form.tel,
                    onValueChange = viewModel::onTelChange,
                    title = "Số điện thoại liên hệ",
                    placeholderText = "09xxxxxx...",
                )

                Spacer(Modifier.height(16.dp))

                NewTextFieldRow(
                    value = form.dob,
                    onValueChange = {},
                    title = "Ngày sinh",
                    placeholderText = "01/01/2000...",
                    readOnly = true,
                    enabled = false,
                    trailingIcon = {
                        Icon(Icons.Outlined.DateRange, null)
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledTextColor = activeColor,
                        disabledBorderColor = borderColor,
                        disabledLabelColor = activeColor,
                        disabledPlaceholderColor = activeColor.copy(alpha = 0.6f),
                        disabledTrailingIconColor = iconColor,
                        disabledLeadingIconColor = iconColor,
                        disabledContainerColor = Color.Transparent,
                        focusedBorderColor = borderColor,
                        unfocusedBorderColor = borderColor,
                        unfocusedTextColor = activeColor
                    ),
                    modifier = Modifier.clickable {
                        showDateDialog = true
                    }
                )

                Spacer(Modifier.height(16.dp))

                NewTextFieldRow(
                    value = form.location,
                    onValueChange = {},
                    title = "Nơi ở",
                    placeholderText = "282 Triều Khúc...",
                    readOnly = true,
                    enabled = false,
                    trailingIcon = {
                        Icon(Icons.Outlined.LocationOn, null)
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledTextColor = activeColor,
                        disabledBorderColor = borderColor,
                        disabledLabelColor = activeColor,
                        disabledPlaceholderColor = activeColor.copy(alpha = 0.6f),
                        disabledTrailingIconColor = iconColor,
                        disabledLeadingIconColor = iconColor,
                        disabledContainerColor = Color.Transparent,
                        focusedBorderColor = borderColor,
                        unfocusedBorderColor = borderColor,
                        unfocusedTextColor = activeColor
                    ),
                    modifier = Modifier.clickable {
                        navController.navigate(AppRoutes.MAP_SCREEN)
                    }
                )

                Spacer(Modifier.height(32.dp))
            }

            item {
                Button(
                    onClick = {
                        showConfirmDialog = true
                    },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(R.color.light_orange_icon)
                    ),
                    contentPadding = PaddingValues(vertical = 20.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.save_title), color = Color.White)
                }
                Spacer(Modifier.height(32.dp))
            }
        }

        if (showMap) {
            Box(Modifier.fillMaxSize()) {
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }

        if (showConfirmDialog) {
            AlertDialog(
                title = { Text("Xác nhận lưu thay đổi") },
                text = { Text("Bạn có chắc chắn muốn lưu những thay đổi này không?") },
                onDismissRequest = { showConfirmDialog = false },
                dismissButton = { Button(onClick = { showConfirmDialog = false }) { Text("Huỷ") } },
                confirmButton = {
                    Button(onClick = {
                        showConfirmDialog = false
                        viewModel.updateProfile()
                    }) {
                        Text("Xác nhận")
                    }
                }
            )
        }

        if (showDateDialog) {
            DatePickerModal(
                onDateSelected = { time ->
                    time?.let {
                        val newDate = TimeUtils.formatDateTimeFull(it)
                        viewModel.onDobChange(newDate)
                    }
                },
                onDismiss = {
                    showDateDialog = false
                })
        }
    }

    when (uiState) {
        is ProfileDetailUiState.Error -> {}

        ProfileDetailUiState.Idle -> {}
        ProfileDetailUiState.Loading -> {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(Color.LightGray.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                CircleLoadingIndicator()
            }
        }

        is ProfileDetailUiState.Success -> {}
    }
}
