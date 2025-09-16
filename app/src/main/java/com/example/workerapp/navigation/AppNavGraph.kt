package com.example.workerapp.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.workerapp.presentation.screens.notification.NotificationScreen
import com.example.workerapp.presentation.screens.profile.ProfileScreen
import com.example.workerapp.presentation.screens.service.ServiceDetailScreen
import com.example.workerapp.presentation.screens.service.ServiceViewModel
import com.example.workerapp.presentation.screens.splash.SplashScreen
import com.example.workerapp.ui.authen.AuthViewModel
import com.example.workerapp.ui.authen.LoginScreen
import com.example.workerapp.ui.authen.RegisterScreen
import com.example.workerapp.ui.calendar.CalendarScreen
import com.example.workerapp.ui.calendar.CalendarViewModel
import com.example.workerapp.ui.detail.cleaning.CleaningDetailScreen
import com.example.workerapp.ui.detail.cleaning.CleaningViewModel
import com.example.workerapp.ui.detail.healcare.HealthcareDetailScreen
import com.example.workerapp.ui.detail.healcare.HealthcareViewModel
import com.example.workerapp.ui.home.HomeScreen
import com.example.workerapp.ui.income.IncomeScreen
import com.example.workerapp.utils.ServiceType

@Composable
fun AppNavHost(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    startDestination: String,
    innerPadding: PaddingValues
) {
    NavHost(
        modifier = Modifier.padding(innerPadding),
        navController = navController,
        startDestination = startDestination,
    ) {
        composable(AppRoutes.SPLASH) {
            SplashScreen(navController)
        }

        composable(AppRoutes.LOGIN) {
            LoginScreen(navController = navController, viewModel = authViewModel)
        }

        composable(AppRoutes.REGISTER) {
            RegisterScreen(navController = navController, viewModel = authViewModel)
        }

        composable(AppRoutes.HOME) {
            HomeScreen(navController = navController)
        }
        composable(AppRoutes.CALENDAR) {
            val calendarViewModel: CalendarViewModel = viewModel()
            CalendarScreen(
                viewModel = calendarViewModel,
                navController = navController
            )
        }
        composable(AppRoutes.INCOME) {
            IncomeScreen()
        }
        composable(AppRoutes.NOTIFICATION) {
            NotificationScreen()
        }
        composable(AppRoutes.PROFILE) {
            ProfileScreen(navController = navController)
        }

        composable(
            route = "${AppScreen.SERVICE_SCREEN}/{${DestinationArgs.SERVICE_TYPE}}",
            arguments = listOf(navArgument(DestinationArgs.SERVICE_TYPE) {
                type = NavType.StringType
            })
        ) { backStackEntry ->

            val serviceType =
                backStackEntry.arguments?.getString(DestinationArgs.SERVICE_TYPE)
                    ?: ServiceType.CleaningType
            val serviceViewModel: ServiceViewModel = viewModel()
            ServiceDetailScreen(
                serviceType = serviceType,
                viewModel = serviceViewModel,
                navController = navController
            )
        }
        composable(
            route = "${AppScreen.CLEANING_SCREEN}/{${DestinationArgs.JOB_ID}}",
            arguments = listOf(navArgument(DestinationArgs.JOB_ID) { type = NavType.StringType })
        ) {
            val cleaningViewModel: CleaningViewModel = viewModel()
            val cleaningUid = it.arguments?.getString(DestinationArgs.JOB_ID) ?: ""
            CleaningDetailScreen(
                cleaningUid = cleaningUid,
                viewmodel = cleaningViewModel,
                navController = navController
            )
        }
        composable(
            route = "${AppScreen.HEALTHCARE_SCREEN}/{${DestinationArgs.JOB_ID}}",
            arguments = listOf(navArgument(DestinationArgs.JOB_ID) { type = NavType.StringType })
        ) {
            val healthcareUid = it.arguments?.getString(DestinationArgs.JOB_ID) ?: ""
            val healthcareViewModel: HealthcareViewModel = viewModel()
            HealthcareDetailScreen(
                healthcareUid = healthcareUid,
                viewModel = healthcareViewModel,
                navController = navController
            )
        }
    }
}
