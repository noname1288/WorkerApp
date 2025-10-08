package com.example.workerapp.ui.base

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.workerapp.R
import com.example.workerapp.navigation.AppNavHost
import com.example.workerapp.navigation.AppRoutes
import com.example.workerapp.navigation.NavItem
import com.example.workerapp.presentation.screens.authen.AuthViewModel
import com.example.workerapp.presentation.screens.profile.ProfileViewModel
import com.example.workerapp.utils.ext.safeNavigate
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun BaseScreen() {

    val startDestination = AppRoutes.SPLASH

    val showBottomBar = listOf(
        AppRoutes.HOME,
        AppRoutes.CALENDAR,
        AppRoutes.INCOME,
        AppRoutes.NOTIFICATION,
        AppRoutes.PROFILE
    )

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    LaunchedEffect(currentRoute) {
        Log.d("BaseScreen", "Current route: $currentRoute")
    }

    /* *
    * Shared ViewModel
    * */
    val authViewModel = hiltViewModel<AuthViewModel>()

    val profileViewModel = hiltViewModel<ProfileViewModel>()


    val systemUiController = rememberSystemUiController()
    val useDarkIcons = true // vì nền trắng nên dùng icon tối

    SideEffect {
        systemUiController.setSystemBarsColor(
            color = Color.White,
            darkIcons = useDarkIcons
        )
    }

    Scaffold(
        bottomBar = {
            val isShowBottomBar = currentRoute != null && showBottomBar.contains(currentRoute)
            if (isShowBottomBar)
                CustomNavigationBar(
                    selectedRoute = currentRoute,
                    onItemSelected = { route ->
                        navController.safeNavigate(route, popUpToRoute = AppRoutes.HOME, restore = true)
                    }
                )
        },
        containerColor = colorResource(R.color.bg_gray)
    ) { innerPadding ->
        AppNavHost(
            navController,
            authViewModel,
            profileViewModel,
            startDestination,
            innerPadding
        )
    }
}

@Composable
fun CustomNavigationBar(
    selectedRoute: String,
    onItemSelected: (String) -> Unit
) {
    val navItemList = listOf<NavItem>(
        NavItem(
            stringResource(R.string.home_title),
            R.drawable.ic_filled_home,
            R.drawable.ic_home,
            true,
            0,
            AppRoutes.HOME
        ),
        NavItem(
            stringResource(R.string.calendar_title),
            R.drawable.ic_filled_calendar,
            R.drawable.ic_calendar,
            false,
            0,
            AppRoutes.CALENDAR
        ),
        NavItem(
            stringResource(R.string.income_title),
            R.drawable.ic_filled_money,
            R.drawable.ic_money,
            false,
            0,
            AppRoutes.INCOME
        ),
        NavItem(
            stringResource(R.string.notification_title),
            R.drawable.ic_filled_notification,
            R.drawable.ic_notification,
            false,
            2,
            AppRoutes.NOTIFICATION
        ),
        NavItem(
            stringResource(R.string.profile_title),
            R.drawable.ic_filled_person,
            R.drawable.ic_person,
            false,
            0,
            AppRoutes.PROFILE
        ),
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(bottom = 16.dp, top = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        navItemList.forEach { item ->
            val isSelected = item.route == selectedRoute
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(4.dp)
                    .clickable {
                        onItemSelected(item.route)
                    }
            ) {
                BadgedBox(
                    badge = {
                        if (item.badgeCount != 0) {
                            Badge { Text(item.badgeCount.toString()) }
                        } else if (item.hasNews) {
                            Badge()
                        }
                    }
                ) {
                    Icon(
                        painter = if (isSelected) painterResource(item.selectedIcon) else painterResource(
                            item.unselectedIcon
                        ),
                        contentDescription = null,
                        tint = if (isSelected) colorResource(R.color.orange_primary) else colorResource(
                            R.color.gray
                        ),
                        modifier = Modifier.height(24.dp),

                        )
                }
                Spacer(Modifier.height(2.dp))
                Text(
                    item.label, fontSize = 12.sp,
                    color = if (isSelected) colorResource(R.color.orange_primary) else colorResource(
                        R.color.gray
                    ),
                    maxLines = 1,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.width(65.dp),
                )
            }
        }
    }

}

