package com.example.workerapp.navigation

object AppScreen{
    const val SERVICE_SCREEN = "service_detail"
    const val CLEANING_SCREEN = "cleaning_detail"
    const val HEALTHCARE_SCREEN = "healthcare_detail"
    const val MAINTENANCE_SCREEN = "maintenance_detail"
    const val NOTIFICATION_SCREEN = "notification_detail"
    const val CHAT_SCREEN = "chat_detail"
}

object DestinationArgs{
    const val SERVICE_TYPE = "serviceType"
    const val JOB_ID = "jobId"
    const val ONLY_WATCH = "onlyWatch"
    const val NOTIFICATION_ID = "notificationId"
    const val CHAT_ID = "chatId"
    const val PARTNER_NAME = "partnerName"
    const val PARTNER_AVATAR = "partnerAvt"

}

object AppRoutes {
    const val SPLASH = "splash"
    const val HOME = "home"
    const val CALENDAR = "calendar"
    const val INCOME = "income"
    const val NOTIFICATION = "notification"
    const val PROFILE = "profile"
    const val PROFILE_DETAIL = "profile_detail"

    const val LOGIN = "login"
    const val REGISTER = "register"
    const val CHANGE_PASSWORD = "change_password"
    const val FORGOT_PASSWORD = "forgot_password"


    const val SERVICE_DETAIL = "${AppScreen.SERVICE_SCREEN}/%s"
    const val CLEANING_DETAIL = "${AppScreen.CLEANING_SCREEN}/%s/%b"
    const val HEALTHCARE_DETAIL = "${AppScreen.HEALTHCARE_SCREEN}/%s/%b"
    const val MAINTENANCE_DETAIL = "${AppScreen.MAINTENANCE_SCREEN}/%s/%b"
    const val CHAT_DETAIL = "${AppScreen.CHAT_SCREEN}/%s/%s/%s"

    const val LIST_APPLICATIONS = "list_applications"

    const val REVIEW_SCREEN = "review"
    const val POLICY_SCREEN = "policy"
    const val MAP_SCREEN = "map_screen"

}
