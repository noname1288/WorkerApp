package com.example.workerapp.presentation.screens.forgot_password

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.workerapp.R
import com.example.workerapp.navigation.AppRoutes
import com.example.workerapp.presentation.screens.change_password.PasswordTextField
import com.example.workerapp.utils.components.CircleLoadingIndicator
import com.example.workerapp.utils.ext.popBackIfCan

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RequireNewPasswordScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: ForgotPasswordViewModel
) {
    val context = LocalContext.current

    val uiState by viewModel.resetPasswordUiState.collectAsState()
    val isLoading = uiState.isLoading
    val success = uiState.success

    val scrollState = rememberScrollState()

    var showConfirmDialog by remember { mutableStateOf(false) }
    var newPassword by remember { mutableStateOf("") }
    var confirmNewPassword by remember { mutableStateOf("") }

    val isPasswordValid = newPassword.length >= 8
    val isConfirmMatch = confirmNewPassword == newPassword
    val canSubmit = isPasswordValid && isConfirmMatch

    LaunchedEffect(uiState.error) {
        uiState.error?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }

    when {
        isLoading -> {
            CircleLoadingIndicator()
        }

        success -> {
            navController.navigate(AppRoutes.LOGIN) {
                popUpTo(0)
            }
        }

        else -> {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .imePadding()
                    .background(Color.White)
                    .padding(16.dp),
                horizontalAlignment = Alignment.Start
            ) {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            stringResource(R.string.request_new_password_title),
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

                Spacer(Modifier.height(64.dp))

                PasswordTextField(
                    title = "Mật khẩu mới",
                    placeholderText = "Nhập mật khẩu mới",
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Next
                    ),
                    onValueChange = { newPassword = it }
                )
                if (!isPasswordValid && newPassword.isNotEmpty()) {
                    Text(
                        text = "Mật khẩu phải có ít nhất 8 ký tự",
                        color = Color.Red,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                Spacer(Modifier.height(16.dp))

                PasswordTextField(
                    title = "Nhập lại mật khẩu mới",
                    placeholderText = "Nhập lại mật khẩu mới",
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    onValueChange = { confirmNewPassword = it }
                )
                if (!isConfirmMatch && confirmNewPassword.isNotEmpty()) {
                    Text(
                        text = "Mật khẩu nhập lại không khớp",
                        color = Color.Red,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                Spacer(Modifier.height(32.dp))

                Button(
                    onClick = {
                        showConfirmDialog = true

                        viewModel.onNewPasswordChange(confirmNewPassword)
                    },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (canSubmit)
                            colorResource(R.color.light_orange_icon)
                        else
                            colorResource(R.color.light_gray)
                    ),
                    contentPadding = PaddingValues(vertical = 20.dp),
                    modifier = Modifier.fillMaxWidth(),
                    enabled = canSubmit
                ) {
                    Text(stringResource(R.string.save_title), color = Color.White)
                }

                Spacer(Modifier.height(32.dp))

                if (showConfirmDialog) {
                    AlertDialog(
                        title = { Text("Xác nhận thay đổi mật khẩu") },
                        onDismissRequest = { showConfirmDialog = false },
                        confirmButton = {
                            Button(onClick = {
                                showConfirmDialog = false

                                viewModel.submitNewPassword(confirmNewPassword)
                            }) { Text("Thay đổi") }
                        },
                        dismissButton = {
                            Button(onClick = { showConfirmDialog = false }) { Text("Huỷ") }
                        }
                    )
                }
            }
        }
    }
}
