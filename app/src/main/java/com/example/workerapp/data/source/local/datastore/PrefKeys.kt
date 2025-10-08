package com.example.workerapp.data.source.local.datastore

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object PrefKeys {
    val PREFS_NAME = "app_preferences"
    val ACCESS_TOKEN = stringPreferencesKey("access_token")
    val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
    val DARK_MODE = booleanPreferencesKey("dark_mode")
    val FCM_TOKEN = stringPreferencesKey("fcm_token")
    val USER_NOTIFICATION = booleanPreferencesKey("user_notification")
}
