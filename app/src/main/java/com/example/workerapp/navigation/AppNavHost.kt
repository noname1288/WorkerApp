package com.example.workerapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.workerapp.ui.calendar.CalendarScreen
import com.example.workerapp.ui.detail.cleaning.CleaningDetailScreen
import com.example.workerapp.ui.detail.healcare.HealthcareDetailScreen
import com.example.workerapp.ui.home.HomeScreen
import com.example.workerapp.ui.income.IncomeScreen
import com.example.workerapp.ui.login.LoginScreen
import com.example.workerapp.ui.login.AuthViewModel
import com.example.workerapp.ui.notification.NotificationScreen
import com.example.workerapp.ui.profile.ProfileScreen

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
        composable(AppRoutes.CALENDAR) {
            CalendarScreen(navController = navHostController)
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
