package com.example.workerapp.presentation.screens.splash

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.workerapp.R
import com.example.workerapp.navigation.AppRoutes
import com.example.workerapp.presentation.screens.authen.AuthViewModel
import com.example.workerapp.presentation.screens.authen.AuthenticationUIState
import com.example.workerapp.utils.cached.UserSession
import com.example.workerapp.utils.ext.safeNavigate
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController, viewModel: AuthViewModel) {
    var visible by remember { mutableStateOf(false) }

    val splashState by viewModel.splashState.collectAsState()


    LaunchedEffect(Unit) {
        visible = true
        delay(3000)
        viewModel.checkUserLoggedIn()
    }

    when (splashState) {
        is AuthenticationUIState.Success -> {
            Log.d("SplashScreen", "User is logged in, navigating to Home")

            //update user session
            val user = (splashState as AuthenticationUIState.Success).userProfile
            UserSession.saveState(user.uid, user.username, user.email, user.avatar)

            navController.safeNavigate(
                AppRoutes.HOME,
                AppRoutes.SPLASH,
                inclusive = true,
                restore = false
            )
        }

        is AuthenticationUIState.Error -> {
            Log.d("SplashScreen", "User is not logged in, navigating to Login")
            navController.safeNavigate(
                AppRoutes.LOGIN,
                AppRoutes.SPLASH,
                inclusive = true,
                restore = false
            )
        }
        AuthenticationUIState.Idle -> {
            // Do nothing, wait for the state to change
            Log.d("SplashScreen", "Idle state")
        }
        AuthenticationUIState.Loading -> {
            // Optionally show a loading indicator
            Log.d("SplashScreen", "Loading state")
        }
    }

    Box(Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.bg_splash),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
        )
        Image(
            painter =
                painterResource(R.drawable.bg_splash_2),
            null,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .fillMaxWidth()
                .padding(bottom = 24.dp)
        )
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(
                animationSpec = tween(
                    durationMillis = 2000 // thời gian 2 giây
                )
            ) + expandIn(
                animationSpec = tween(2000)
            ),
            exit = fadeOut(
                animationSpec = tween(
                    durationMillis = 2000
                )
            ) + shrinkOut(
                animationSpec = tween(2000)
            ),
            modifier = Modifier.align(Alignment.Center)
        ) {
            Text(
                "Good Jobs",
                style = MaterialTheme.typography.headlineLarge.copy(
                    color = colorResource(R.color.orange_primary),
                    fontWeight = FontWeight.Bold
                ),
            )
        }
    }
}
