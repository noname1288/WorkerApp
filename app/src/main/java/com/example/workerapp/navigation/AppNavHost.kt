package com.example.workerapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.workerapp.view.detail.cleaning.CleaningDetailScreen
import com.example.workerapp.view.detail.healcare.HealthcareDetailScreen
import com.example.workerapp.view.home.HomeScreen
import com.example.workerapp.view.income.IncomeScreen
import com.example.workerapp.view.login.LoginScreen
import com.example.workerapp.view.login.AuthViewModel
import com.example.workerapp.view.notification.NotificationScreen
import com.example.workerapp.view.profile.ProfileScreen

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    authViewModel: AuthViewModel,
    startDestination: String
) {
    NavHost(
        modifier = modifier,
        navController = navHostController,
        startDestination = startDestination,
    ) {
        composable(AppRoutes.LOGIN) {
            LoginScreen(navController = navHostController, viewModel = authViewModel)
        }

        composable(AppRoutes.HOME) {
            HomeScreen(navController = navHostController)
        }
        composable(AppRoutes.INCOME) {
            IncomeScreen()
        }
        composable(AppRoutes.NOTIFICATION) {
            NotificationScreen()
        }
        composable(AppRoutes.PROFILE) {
            ProfileScreen()
        }

        composable(AppRoutes.CLEANING_DETAIL) {
            CleaningDetailScreen()
        }
        composable (AppRoutes.HEALTHCARE_DETAIL) {
            HealthcareDetailScreen()
        }
    }
}
