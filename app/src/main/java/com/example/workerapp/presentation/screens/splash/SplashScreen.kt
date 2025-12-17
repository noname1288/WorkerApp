package com.example.workerapp.presentation.screens.splash

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.example.workerapp.R
import com.example.workerapp.navigation.AppRoutes
import com.example.workerapp.presentation.screens.authen.AuthViewModel
import com.example.workerapp.presentation.screens.authen.AuthenticationUIState
import com.example.workerapp.utils.ext.safeNavigate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

@Composable
fun SplashScreen(navController: NavController, viewModel: AuthViewModel) {
    var visible by remember { mutableStateOf(false) }
    val splashState by viewModel.splashState.collectAsState()
    val loginStatus by viewModel.loggedIn.collectAsState()
    val context = LocalContext.current

    // Khi splash vừa khởi tạo
    LaunchedEffect(Unit) {
        visible = true
        delay(2000)
        viewModel.checkLoginStatus()
    }

    // Observe token expired event
    LaunchedEffect(Unit) {
        viewModel.tokenExpiredEvent.collect {
            // Hiển thị dialog
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Phiên đăng nhập đã hết hạn", Toast.LENGTH_LONG).show()
            }

            viewModel.logout()

            navController.safeNavigate(
                AppRoutes.LOGIN,
                AppRoutes.SPLASH,
                inclusive = true,
                restore = false
            )
        }
    }

    when (splashState) {
        is AuthenticationUIState.Success -> {
            navController.safeNavigate(
                AppRoutes.HOME,
                AppRoutes.SPLASH,
                inclusive = true,
                restore = false
            )
        }

        is AuthenticationUIState.Error -> {
            navController.safeNavigate(
                AppRoutes.LOGIN,
                AppRoutes.SPLASH,
                inclusive = true,
                restore = false
            )
        }

        AuthenticationUIState.Idle, AuthenticationUIState.Loading -> {
        }
    }

    SplashContent(visible)
}

@Composable
private fun SplashContent(visible: Boolean) {
    Box(Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.bg_splash),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
        )
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(tween(2000)) + expandIn(tween(2000)),
            exit = fadeOut(tween(2000)) + shrinkOut(tween(2000)),
            modifier = Modifier.align(Alignment.Center)
        ) {
            Text(
                "Good Jobs",
                style = MaterialTheme.typography.headlineLarge.copy(
                    color = colorResource(R.color.orange_primary),
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}
