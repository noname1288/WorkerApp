package com.example.workerapp.navigation

object AppScreen{
    const val SERVICE_SCREEN = "service_detail"
    const val CLEANING_SCREEN = "cleaning_detail"
    const val HEALTHCARE_SCREEN = "healthcare_detail"
}

object DestinationArgs{
    const val SERVICE_TYPE = "serviceType"
    const val JOB_ID = "jobId"
    const val ONLY_WATCH = "onlyWatch"
}

object AppRoutes {
    const val SPLASH = "splash"
    const val HOME = "home"
    const val CALENDAR = "calendar"
    const val INCOME = "income"
    const val NOTIFICATION = "notification"
    const val PROFILE = "profile"

    const val LOGIN = "login"
    const val REGISTER = "register"

    const val SERVICE_DETAIL = "${AppScreen.SERVICE_SCREEN}/%s"
    const val CLEANING_DETAIL = "${AppScreen.CLEANING_SCREEN}/%s/%b"
    const val HEALTHCARE_DETAIL = "${AppScreen.HEALTHCARE_SCREEN}/%s/%b"

    const val LIST_APPLICATIONS = "list_applications"
}
