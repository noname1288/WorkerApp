package com.example.workerapp.data.source.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.example.workerapp.data.source.TokenDataSource
import com.example.workerapp.data.source.local.datastore.PrefKeys
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

class TokenLocalImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : TokenDataSource.Local {

    override fun getAccessToken(): Flow<String?> =
        dataStore.data.map { prefs -> prefs[PrefKeys.ACCESS_TOKEN] }


    override fun getRefreshToken(): Flow<String?> =
        dataStore.data.map { prefs -> prefs[PrefKeys.REFRESH_TOKEN] }

    override fun getFcmToken(): Flow<String?> =
        dataStore.data.map { prefs -> prefs[PrefKeys.FCM_TOKEN] }


    override suspend fun saveAccessToken(accessToken: String) {
        dataStore.edit { prefs ->
            prefs[PrefKeys.ACCESS_TOKEN] = accessToken
        }
    }

    override suspend fun saveRefreshToken(refreshToken: String) {
        dataStore.edit { prefs ->
            prefs[PrefKeys.REFRESH_TOKEN] = refreshToken
        }
    }

    override suspend fun saveFcmToken(fcmToken: String) {
        dataStore.edit { prefs ->
            prefs[PrefKeys.FCM_TOKEN] = fcmToken
        }
    }

    override suspend fun clearAuthTokens() {
        dataStore.edit { prefs ->
            prefs.remove(PrefKeys.ACCESS_TOKEN)
            prefs.remove(PrefKeys.REFRESH_TOKEN)
        }
    }
}
