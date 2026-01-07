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
import com.example.workerapp.presentation.screens.application.ApplicationViewModel
import com.example.workerapp.presentation.screens.application.ApplicationsScreen
import com.example.workerapp.presentation.screens.authen.AuthViewModel
import com.example.workerapp.presentation.screens.authen.LoginScreen
import com.example.workerapp.presentation.screens.authen.RegisterScreen
import com.example.workerapp.presentation.screens.bot.ChatBotScreen
import com.example.workerapp.presentation.screens.bot.ChatbotViewModel
import com.example.workerapp.presentation.screens.calendar.CalendarScreen
import com.example.workerapp.presentation.screens.change_password.ChangePasswordScreen
import com.example.workerapp.presentation.screens.change_password.ChangePasswordViewModel
import com.example.workerapp.presentation.screens.detail_job.cleaning.CleaningDetailScreen
import com.example.workerapp.presentation.screens.detail_job.healthcare.HealthcareDetailScreen
import com.example.workerapp.presentation.screens.detail_job.healthcare.HealthcareViewModel
import com.example.workerapp.presentation.screens.detail_job.maintenance.MaintenanceDetailScreen
import com.example.workerapp.presentation.screens.detail_job.maintenance.MaintenanceViewModel
import com.example.workerapp.presentation.screens.home.HomeScreen
import com.example.workerapp.presentation.screens.income.IncomeScreen
import com.example.workerapp.presentation.screens.map.MapScreen
import com.example.workerapp.presentation.screens.notification_chat.NotificationChatViewModel
import com.example.workerapp.presentation.screens.notification_chat.NotificationChatScreen
import com.example.workerapp.presentation.screens.notification_chat.chat.ChatDetailScreen
import com.example.workerapp.presentation.screens.notification_chat.chat.ChatDetailViewModel
import com.example.workerapp.presentation.screens.policy.PolicyScreen
import com.example.workerapp.presentation.screens.policy.PolicyViewModel
import com.example.workerapp.presentation.screens.profile.ProfileScreen
import com.example.workerapp.presentation.screens.profile.ProfileViewModel
import com.example.workerapp.presentation.screens.profile.detail.ProfileDetailScreen
import com.example.workerapp.presentation.screens.profile.detail.ProfileDetailViewModel
import com.example.workerapp.presentation.screens.review.ReviewScreen
import com.example.workerapp.presentation.screens.review.ReviewViewModel
import com.example.workerapp.presentation.screens.service.ServiceDetailScreen
import com.example.workerapp.presentation.screens.service.ServiceViewModel
import com.example.workerapp.presentation.screens.splash.SplashScreen
import com.example.workerapp.ui.calendar.CalendarViewModel
import com.example.workerapp.presentation.screens.detail_job.cleaning.CleaningViewModel
import com.example.workerapp.ui.home.HomeViewModel
import com.example.workerapp.utils.ServiceType
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun AppNavHost(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    profileViewModel: ProfileViewModel,
    rootViewModel: NotificationChatViewModel,
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

        composable(AppRoutes.CHANGE_PASSWORD) {
            val changePasswordViewModel = hiltViewModel<ChangePasswordViewModel>()
            ChangePasswordScreen(navController = navController, viewmodel = changePasswordViewModel)
        }

        forgotPasswordGraph(navController)

        composable(AppRoutes.HOME) {
            val homeViewModel = hiltViewModel<HomeViewModel>()
            HomeScreen(
                navController = navController,
                viewModel = homeViewModel,
                notificationViewModel = rootViewModel
            )
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

            NotificationChatScreen(
                rootViewModel = rootViewModel,
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

        composable(AppRoutes.PROFILE_DETAIL) {
            val profileDetailViewModel = hiltViewModel<ProfileDetailViewModel>()
            ProfileDetailScreen(navController = navController, viewModel = profileDetailViewModel)
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

        composable(
            route = "${AppScreen.MAINTENANCE_SCREEN}/{${DestinationArgs.JOB_ID}}/{${DestinationArgs.ONLY_WATCH}}",
            arguments = listOf(
                navArgument(DestinationArgs.JOB_ID) { type = NavType.StringType },
                navArgument(DestinationArgs.ONLY_WATCH) { type = NavType.BoolType }
            )
        ) {
            val maintenanceViewModel = hiltViewModel<MaintenanceViewModel>()

            val maintenanceUid = it.arguments?.getString(DestinationArgs.JOB_ID) ?: ""
            val onlyWatch = it.arguments?.getBoolean(DestinationArgs.ONLY_WATCH) ?: false

            MaintenanceDetailScreen(
                maintenanceUid = maintenanceUid,
                isOnlyWatch = onlyWatch,
                navController = navController,
                viewModel = maintenanceViewModel
            )
        }

        composable(AppRoutes.LIST_APPLICATIONS) {
            val applicationViewModel = hiltViewModel<ApplicationViewModel>()
            ApplicationsScreen(viewModel = applicationViewModel, navController = navController)
        }

        composable(AppRoutes.REVIEW_SCREEN) {
            val reviewViewModel = hiltViewModel<ReviewViewModel>()
            ReviewScreen(navController = navController, viewModel = reviewViewModel)
        }

        composable(AppRoutes.POLICY_SCREEN) {
            val policyViewModel = hiltViewModel<PolicyViewModel>()
            PolicyScreen(viewModel = policyViewModel, navController = navController)
        }

        composable(
            route = "${AppScreen.CHAT_SCREEN}/{${DestinationArgs.CHAT_ID}}/{${DestinationArgs.PARTNER_NAME}}/{${DestinationArgs.PARTNER_AVATAR}}",
            arguments = listOf(
                navArgument(DestinationArgs.CHAT_ID) { type = NavType.StringType },
                navArgument(DestinationArgs.PARTNER_NAME) { type = NavType.StringType },
                navArgument(DestinationArgs.PARTNER_AVATAR) { type = NavType.StringType }
            )
        ) {
            val chatId = it.arguments?.getString(DestinationArgs.CHAT_ID) ?: ""
            val partnerName = URLDecoder.decode(
                it.arguments?.getString(DestinationArgs.PARTNER_NAME) ?: "",
                StandardCharsets.UTF_8.toString()
            )
            val partnerAvatar = URLDecoder.decode(
                it.arguments?.getString(DestinationArgs.PARTNER_AVATAR) ?: "",
                StandardCharsets.UTF_8.toString()
            )

            val chatDetailViewModel = hiltViewModel<ChatDetailViewModel>()

            ChatDetailScreen(
                navController = navController,
                roomId = chatId,
                partnerName = partnerName,
                partnerAvatar = partnerAvatar,
                viewModel = chatDetailViewModel
            )
        }

        composable(AppRoutes.MAP_SCREEN) {
            MapScreen(navController = navController)
        }

        composable (AppRoutes.CHATBOT_SCREEN) {
            val viewModel = hiltViewModel<ChatbotViewModel>()

            ChatBotScreen(navController = navController, viewModel = viewModel)
        }
    }
}
