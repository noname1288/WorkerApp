package com.example.workerapp.view.base

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.WorkOutline
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Work
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.workerapp.R
import com.example.workerapp.navigation.AppNavHost
import com.example.workerapp.navigation.AppRoutes
import com.example.workerapp.navigation.NavItem

@Composable
fun BaseScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    var currentRoute = navBackStackEntry?.destination?.route

    val startDestination = AppRoutes.LOGIN
    val showBottomBar = listOf(
        AppRoutes.HOME,
        AppRoutes.INCOME,
        AppRoutes.NOTIFICATION,
        AppRoutes.PROFILE
    )
    val showTopAppBar = emptyList<String>()


    Scaffold(
        topBar = {
            val isShowTopAppBar = currentRoute != null && showTopAppBar.contains(currentRoute)
            if (isShowTopAppBar) {

            }
        },
        bottomBar = {
            val isShowBottomBar = currentRoute != null && showBottomBar.contains(currentRoute)
            if (isShowBottomBar)
                CustomNavigationBar(
                    selectedRoute = currentRoute ?: AppRoutes.HOME,
                    onItemSelected = { route ->
                        navController.navigate(route)
                    }
                )
        },
        floatingActionButton = {

        },
        containerColor = Color.White
    ) { innerPadding ->
        AppNavHost(
            Modifier.padding(innerPadding),
            navController,
            startDestination
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
            Icons.Rounded.Home,
            Icons.Outlined.Home,
            true,
            0,
            AppRoutes.HOME
        ),
        NavItem(
            stringResource(R.string.income_title),
            Icons.Rounded.Work,
            Icons.Outlined.WorkOutline,
            false,
            0,
            AppRoutes.INCOME
        ),
        NavItem(
            stringResource(R.string.notification_title),
            Icons.Rounded.Notifications,
            Icons.Outlined.Notifications,
            false,
            2,
            AppRoutes.NOTIFICATION
        ),
        NavItem(
            stringResource(R.string.profile_title),
            Icons.Rounded.Person,
            Icons.Outlined.Person,
            false,
            0,
            AppRoutes.PROFILE
        ),
    )

    Surface(
        shape = RoundedCornerShape(topEnd = 20.dp, topStart = 20.dp),
        shadowElevation = 12.dp,
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 16.dp),
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
                            imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                            tint = colorResource(R.color.orange),
                            contentDescription = null
                        )
                    }
                    Spacer(Modifier.height(2.dp))
                    Text(
                        item.label, fontSize = 12.sp,
                        maxLines = 1,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.width(70.dp),
                    )
                }
            }
        }
    }
}

