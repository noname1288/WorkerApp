package com.example.workerapp.presentation.screens.forgot_password

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.workerapp.navigation.AppRoutes
import com.example.workerapp.utils.components.CircleLoadingIndicator
import com.example.workerapp.utils.ext.popBackIfCan
import com.example.workerapp.utils.ext.safeNavigate


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RequireCodeScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    codeFromEmail: String,
    viewModel: ForgotPasswordViewModel
) {
    val focusManager = LocalFocusManager.current

    var code by remember { mutableStateOf(List(6) { "" }) }
    val isCodeFilled = code.all { it.isNotEmpty() }
    val correctCode by rememberSaveable { mutableStateOf(codeFromEmail) }

    var isError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        Log.d("RequireCodeScreen", "Code from email: $codeFromEmail")
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- Top Bar ---
        CenterAlignedTopAppBar(
            title = {},
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

        Spacer(modifier = Modifier.height(24.dp))

        // --- Title ---
        Text(
            text = "Nhập mã xác thực",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Vui lòng kiểm tra hộp thư để lấy mã",
            fontSize = 14.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        // --- OTP Input Row ---
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            code.forEachIndexed { index, value ->
                val focusRequester = remember { FocusRequester() }
                val isFocused = remember { mutableStateOf(false) }

                OtpInputField(
                    value = value,
                    onValueChange = {
                        code = code.toMutableList().also { list -> list[index] = it }
                        if (it.isNotEmpty() && index < 5)
                            focusManager.moveFocus(FocusDirection.Next)
                    },
                    modifier = Modifier
                        .focusRequester(focusRequester)
                        .onFocusChanged { isFocused.value = it.isFocused }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // --- Error Message ---
        if (isError) {
            Text(
                text = errorMessage,
                color = Color.Red,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- Resend Code ---
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Bạn chưa nhận được mã?", fontSize = 14.sp)
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "Gửi lại",
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { /* TODO: handle resend */ }
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        // --- Verify button ---
        Button(
            onClick = {
                val enteredCode = code.joinToString("")
                if (enteredCode == correctCode) {
                    isError = false
                    navController.navigate(AppRoutes.REQUIRE_NEW_PASSWORD)
                } else {
                    isError = true
                    errorMessage = "Mã xác thực không chính xác. Vui lòng kiểm tra lại."
                }
            },
            enabled = isCodeFilled,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = MaterialTheme.shapes.extraLarge,
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isCodeFilled) Color.Black else Color.LightGray
            )
        ) {
            Text(
                text = "Verify",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
    }
}
