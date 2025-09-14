package com.example.workerapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.workerapp.ui.authen.AuthViewModel
import com.example.workerapp.ui.authen.LoginScreen
import com.example.workerapp.ui.authen.RegisterScreen
import com.example.workerapp.ui.calendar.CalendarScreen
import com.example.workerapp.ui.detail.cleaning.CleaningDetailScreen
import com.example.workerapp.ui.detail.cleaning.CleaningViewModel
import com.example.workerapp.ui.detail.healcare.HealthcareDetailScreen
import com.example.workerapp.ui.detail.healcare.HealthcareViewModel
import com.example.workerapp.ui.home.HomeScreen
import com.example.workerapp.ui.income.IncomeScreen
import com.example.workerapp.ui.notification.NotificationScreen
import com.example.workerapp.ui.profile.ProfileScreen
import com.example.workerapp.ui.service.ServiceDetailScreen
import com.example.workerapp.ui.service.ServiceViewModel
import com.example.workerapp.utils.ServiceType

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

        composable(AppRoutes.REGISTER) {
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

        composable(
            route = "service_detail/{serviceType}",
            arguments = listOf(navArgument("serviceType") { type = NavType.StringType })
        ) { backStackEntry ->

            val serviceType =
                backStackEntry.arguments?.getString("serviceType") ?: ServiceType.CleaningType
            val serviceViewModel: ServiceViewModel = viewModel()
            ServiceDetailScreen(serviceType, serviceViewModel, navController)
        }
        composable(
            route = "cleaning_detail/{cleaningUid}",
            arguments = listOf(navArgument("cleaningUid") { type = NavType.StringType })
        ) {
            val cleaningViewModel: CleaningViewModel = viewModel()
            val cleaningUid = it.arguments?.getString("cleaningUid") ?: ""
            CleaningDetailScreen(cleaningUid, cleaningViewModel, navController)
        }
        composable(
            route = "healthcare_detail/{healthcareUid}",
            arguments = listOf(navArgument("healthcareUid") { type = NavType.StringType })
        ) {
            val healthcareUid = it.arguments?.getString("healthcareUid") ?: ""
            val healthcareViewModel: HealthcareViewModel = viewModel()
            HealthcareDetailScreen(healthcareUid, healthcareViewModel, navController)
        }
    }
}
