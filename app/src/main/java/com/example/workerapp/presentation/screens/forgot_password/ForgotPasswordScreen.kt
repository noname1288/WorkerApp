package com.example.workerapp.presentation.screens.forgot_password

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.workerapp.R
import com.example.workerapp.navigation.AppRoutes
import com.example.workerapp.presentation.screens.authen.CustomEditTextField
import com.example.workerapp.utils.components.CircleLoadingIndicator
import com.example.workerapp.utils.ext.navigateWithArgs

@Composable
fun ForgotPasswordScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: ForgotPasswordViewModel
) {
    val context = LocalContext.current

    val scrollState = rememberScrollState()
    val uiState by viewModel.uiState.collectAsState()
    val isLoading = uiState.isLoading
    val success = uiState.success
    val codeFromEmail = uiState.codeFromEmail ?: ""


    var email by rememberSaveable { mutableStateOf("") }
    var isEmailValid by rememberSaveable { mutableStateOf(true) }

    // Regex kiểm tra định dạng email
    val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")

    val canSubmit = emailRegex.matches(email)

    LaunchedEffect(uiState.error) {
        uiState.error?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }
    }

    when {
        isLoading -> {
            CircleLoadingIndicator()
        }

        success ->{
            navController.navigateWithArgs(AppRoutes.REQUIRE_CODE_FROM_EMAIL, args = arrayOf(codeFromEmail))
        }

        else -> {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .imePadding()
                    .background(Color.White)
                    .padding(32.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = stringResource(R.string.forgot_password_title),
                    style = TextStyle(
                        fontSize = 25.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = colorResource(R.color.orange_primary)
                    )
                )

                Spacer(Modifier.height(32.dp))

                Text(
                    text = stringResource(R.string.forgot_password_content),
                    style = MaterialTheme.typography.bodyLarge.copy(fontStyle = FontStyle.Italic),
                )

                Spacer(Modifier.height(32.dp))

                CustomEditTextField(
                    leadingIcon = Icons.Default.Email,
                    title = "Email của bạn",
                    placeholderText = "Nhập email",
                    isPasswordTextField = false,
                    onTextChange = {
                        email = it
                        isEmailValid = it.isEmpty() || emailRegex.matches(it)
                    }
                )

                // ⚠️ Hiển thị lỗi khi email không hợp lệ
                if (!isEmailValid && email.isNotEmpty()) {
                    Text(
                        text = "Email không hợp lệ. Vui lòng nhập đúng định dạng.",
                        color = Color.Red,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                Spacer(Modifier.height(64.dp))

                Button(
                    onClick = { viewModel.sendForgotPasswordEmail(email) },
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(vertical = 20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (canSubmit)
                            colorResource(R.color.light_orange_icon)
                        else
                            colorResource(R.color.light_gray)
                    ),
                    enabled = canSubmit,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(stringResource(R.string.request_title), color = Color.White)
                }
            }
        }
    }
}

