package com.example.workerapp.navigation

import androidx.compose.runtime.remember
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.workerapp.presentation.screens.forgot_password.ForgotPasswordScreen
import com.example.workerapp.presentation.screens.forgot_password.ForgotPasswordViewModel
import com.example.workerapp.presentation.screens.forgot_password.RequireCodeScreen
import com.example.workerapp.presentation.screens.forgot_password.RequireNewPasswordScreen

fun NavGraphBuilder.forgotPasswordGraph(navController: NavController) {
    navigation(
        startDestination = AppRoutes.FORGOT_PASSWORD,
        route = GraphRoutes.FORGOT_PASSWORD_GRAPH
    ) {

        composable(AppRoutes.FORGOT_PASSWORD) {
            val parentEntry = remember(navController) {
                navController.getBackStackEntry("forgotPassword")
            }
            val viewModel: ForgotPasswordViewModel = hiltViewModel(parentEntry)

            ForgotPasswordScreen(
                navController = navController,
                viewModel = viewModel
            )
        }

        composable(
            route = "${AppScreen.REQUIRE_CODE_SCREEN}/{${DestinationArgs.CODE_FROM_EMAIL}}",
            arguments = listOf(
                navArgument(DestinationArgs.CODE_FROM_EMAIL) { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val parentEntry = remember(navController) {
                navController.getBackStackEntry("forgotPassword")
            }
            val viewModel: ForgotPasswordViewModel = hiltViewModel(parentEntry)

            val codeFromEmail =
                backStackEntry.arguments?.getString(DestinationArgs.CODE_FROM_EMAIL) ?: ""

            RequireCodeScreen(
                navController = navController,
                viewModel = viewModel,
                codeFromEmail = codeFromEmail
            )
        }

        composable(AppRoutes.REQUIRE_NEW_PASSWORD) {
            val parentEntry = remember(navController) {
                navController.getBackStackEntry("forgotPassword")
            }
            val viewModel: ForgotPasswordViewModel = hiltViewModel(parentEntry)

            RequireNewPasswordScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
    }
}