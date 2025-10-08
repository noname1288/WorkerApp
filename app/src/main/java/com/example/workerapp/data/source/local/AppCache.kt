package com.example.workerapp.data.source.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.example.workerapp.data.source.local.datastore.PrefKeys
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AppCache @Inject constructor(private val dataStore: DataStore<Preferences>) {
    fun getNotifiticationPermission() : Flow<Boolean> =
        dataStore.data.map { prefs -> prefs[PrefKeys.USER_NOTIFICATION] ?: false }

    suspend fun changeNotificationPermission(isGranted: Boolean) {
        dataStore.edit { prefs ->
            prefs[PrefKeys.USER_NOTIFICATION] = isGranted
        }
    }

    fun getDarkMode() : Flow<Boolean> =
        dataStore.data.map { prefs -> prefs[PrefKeys.DARK_MODE] ?: false  }

    suspend fun changeDarkMode(isDarkMode: Boolean) {
        dataStore.edit { preferences ->
            preferences[PrefKeys.DARK_MODE] = isDarkMode
        }
    }
}