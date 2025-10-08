package com.example.workerapp.utils.ext

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.navigation.NavController
import com.example.workerapp.navigation.AppRoutes

fun NavController.safeNavigate(
    route: String,
    popUpToRoute: String = AppRoutes.HOME,
    inclusive: Boolean = false,
    restore: Boolean = true,
) {
    this.navigate(route) {
        popUpTo(popUpToRoute) {
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
    popUpToRoute: String? = null,
    isInclusive: Boolean = false,
    restore: Boolean = true
) {
    // Giả sử route có định dạng như "detail/%s" và args sẽ thay thế %s
    val formattedRoute = String.format(route, *args)
    this.navigate(formattedRoute) {
        if (popUpToRoute != null) {
            popUpTo(popUpToRoute) {
                inclusive = isInclusive
                saveState = restore
            }
        }
        launchSingleTop = true
        restoreState = restore
    }
}

fun openGoogleMap(context: Context, jobAddress: String) {
    if (jobAddress.isNullOrEmpty())
        return

    // Dùng URL của Google Maps Web
    val encodedAddress = Uri.encode(jobAddress)
    val mapUrl = "https://www.google.com/maps/search/?api=1&query=$encodedAddress"

    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(mapUrl))
    context.startActivity(browserIntent)

    context.startActivity(browserIntent)
}

