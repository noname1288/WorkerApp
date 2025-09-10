package com.example.workerapp.navigation

data class NavItem(
    val label: String,
    val selectedIcon: Int,
    val unselectedIcon: Int,
    val hasNews: Boolean = false,
    val badgeCount: Int = 0,
    val route: String,
)
