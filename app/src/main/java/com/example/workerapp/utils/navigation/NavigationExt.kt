package com.example.workerapp.utils.navigation

import androidx.navigation.NavController
import com.example.workerapp.navigation.AppRoutes

fun NavController.safeNavigate(
    route: String,
    popUpToRoute: String = AppRoutes.HOME,
    inclusive: Boolean = false,
    restore : Boolean = true,
){
    this.navigate(route){
        popUpTo(popUpToRoute){
            this.inclusive = inclusive
            this.saveState = restore
        }
        launchSingleTop = true
        restoreState = restore
    }
}

fun NavController.popBackIfCan() {
    if (popBackStack()) {
    }
}

fun NavController.navigateWithArgs(
    route: String,
    vararg args: Any,
    popUpToRoute: String = AppRoutes.HOME,
    isInclusive: Boolean = false,
    restore: Boolean = true
) {
    // Giả sử route có định dạng như "detail/%s" và args sẽ thay thế %s
    val formattedRoute = String.format(route, *args)
    this.navigate(formattedRoute) {
        popUpTo(popUpToRoute) {
            inclusive = isInclusive
            saveState = restore
        }
        launchSingleTop = true
        restoreState = restore
    }
}

