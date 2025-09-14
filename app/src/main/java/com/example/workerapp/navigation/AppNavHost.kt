package com.example.workerapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.workerapp.ui.calendar.CalendarScreen
import com.example.workerapp.ui.detail.cleaning.CleaningDetailScreen
import com.example.workerapp.ui.detail.cleaning.CleaningViewModel
import com.example.workerapp.ui.detail.healcare.HealthcareDetailScreen
import com.example.workerapp.ui.detail.healcare.HealthcareViewModel
import com.example.workerapp.ui.home.HomeScreen
import com.example.workerapp.ui.income.IncomeScreen
import com.example.workerapp.ui.authen.LoginScreen
import com.example.workerapp.ui.authen.AuthViewModel
import com.example.workerapp.ui.authen.RegisterScreen
import com.example.workerapp.ui.notification.NotificationScreen
import com.example.workerapp.ui.profile.ProfileScreen

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    authViewModel: AuthViewModel,
    startDestination: String
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination,
    ) {
        composable(AppRoutes.LOGIN) {
            LoginScreen(navController = navController, viewModel = authViewModel)
        }

        composable (AppRoutes.REGISTER) {
            RegisterScreen(navController = navController, viewModel = authViewModel)
        }

        composable(AppRoutes.HOME) {
            HomeScreen(navController = navController)
        }
        composable(AppRoutes.CALENDAR) {
            CalendarScreen(navController = navController)
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
            val cleaningViewModel : CleaningViewModel = viewModel()
            CleaningDetailScreen(cleaningViewModel, navController)
        }
        composable (AppRoutes.HEALTHCARE_DETAIL) {
            val healthcareViewModel : HealthcareViewModel = viewModel()
            HealthcareDetailScreen(healthcareViewModel, navController)
        }
    }
}
