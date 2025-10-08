package com.example.workerapp.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.workerapp.presentation.screens.authen.AuthViewModel
import com.example.workerapp.presentation.screens.notification.NotificationScreenRoot
import com.example.workerapp.presentation.screens.profile.ProfileScreen
import com.example.workerapp.presentation.screens.service.ServiceDetailScreen
import com.example.workerapp.presentation.screens.service.ServiceViewModel
import com.example.workerapp.presentation.screens.splash.SplashScreen
import com.example.workerapp.presentation.screens.authen.LoginScreen
import com.example.workerapp.presentation.screens.authen.RegisterScreen
import com.example.workerapp.presentation.screens.calendar.CalendarScreen
import com.example.workerapp.ui.calendar.CalendarViewModel
import com.example.workerapp.presentation.screens.detail.cleaning.CleaningDetailScreen
import com.example.workerapp.ui.detail.cleaning.CleaningViewModel
import com.example.workerapp.presentation.screens.detail.healcare.HealthcareDetailScreen
import com.example.workerapp.presentation.screens.detail.healcare.HealthcareViewModel
import com.example.workerapp.presentation.screens.home.HomeScreen
import com.example.workerapp.presentation.screens.income.IncomeScreen
import com.example.workerapp.presentation.screens.notification.NotificationViewModel
import com.example.workerapp.presentation.screens.profile.ProfileViewModel
import com.example.workerapp.presentation.screens.profile.detail.ApplicationsScreen
import com.example.workerapp.ui.home.HomeViewModel
import com.example.workerapp.utils.ServiceType

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun AppNavHost(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    profileViewModel: ProfileViewModel,
    startDestination: String,
    innerPadding: PaddingValues
) {
    NavHost(
        modifier = Modifier.padding(innerPadding),
        navController = navController,
        startDestination = startDestination,
    ) {
        composable(AppRoutes.SPLASH) {
            SplashScreen(navController, authViewModel)
        }

        composable(AppRoutes.LOGIN) {
            LoginScreen(navController = navController, viewModel = authViewModel)
        }

        composable(AppRoutes.REGISTER) {
            RegisterScreen(navController = navController, viewModel = authViewModel)
        }

        composable(AppRoutes.HOME) {
            val homeViewModel = hiltViewModel<HomeViewModel>()
            HomeScreen(navController = navController, viewModel = homeViewModel)
        }
        composable(AppRoutes.CALENDAR) {
            val calendarViewModel = hiltViewModel<CalendarViewModel>()
            CalendarScreen(
                viewModel = calendarViewModel,
                navController = navController
            )
        }
        composable(AppRoutes.INCOME) {
            IncomeScreen()
        }
        composable(AppRoutes.NOTIFICATION) {
            val notificationViewModel = hiltViewModel<NotificationViewModel>()
            NotificationScreenRoot(
                notificationViewModel = notificationViewModel,
                navController = navController
            )
        }
        composable(AppRoutes.PROFILE) {
            ProfileScreen(
                navController = navController,
                authViewModel = authViewModel,
                profileViewModel = profileViewModel
            )
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
            val serviceViewModel = hiltViewModel<ServiceViewModel>()
            ServiceDetailScreen(
                serviceType = serviceType,
                viewModel = serviceViewModel,
                navController = navController
            )
        }
        composable(
            route = "${AppScreen.CLEANING_SCREEN}/{${DestinationArgs.JOB_ID}}/{${DestinationArgs.ONLY_WATCH}}",
            arguments = listOf(
                navArgument(DestinationArgs.JOB_ID) { type = NavType.StringType },
                navArgument(DestinationArgs.ONLY_WATCH) { type = NavType.BoolType }
            )
        ) {
            val cleaningViewModel = hiltViewModel<CleaningViewModel>()

            val cleaningUid = it.arguments?.getString(DestinationArgs.JOB_ID) ?: ""
            val onlyWatch = it.arguments?.getBoolean(DestinationArgs.ONLY_WATCH) ?: false

            CleaningDetailScreen(
                cleaningUid = cleaningUid,
                isOnlyWatch = onlyWatch,
                viewModel = cleaningViewModel,
                navController = navController
            )
        }
        composable(
            route = "${AppScreen.HEALTHCARE_SCREEN}/{${DestinationArgs.JOB_ID}}/{${DestinationArgs.ONLY_WATCH}}",
            arguments = listOf(
                navArgument(DestinationArgs.JOB_ID) { type = NavType.StringType },
                navArgument(DestinationArgs.ONLY_WATCH) { type = NavType.BoolType }
            )
        ) {
            val healthcareViewModel = hiltViewModel<HealthcareViewModel>()

            val healthcareUid = it.arguments?.getString(DestinationArgs.JOB_ID) ?: ""
            val onlyWatch = it.arguments?.getBoolean(DestinationArgs.ONLY_WATCH) ?: false

            HealthcareDetailScreen(
                healthcareUid = healthcareUid,
                isOnlyWatch = onlyWatch,
                viewModel = healthcareViewModel,
                navController = navController
            )
        }

        composable(AppRoutes.LIST_APPLICATIONS) {
            ApplicationsScreen(viewModel = profileViewModel)
        }
    }
}
