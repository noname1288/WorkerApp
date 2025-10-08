package com.example.workerapp.data.repository

import android.util.Log
import com.example.workerapp.data.TokenRepository
import com.example.workerapp.data.source.TokenDataSource
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject
import javax.inject.Singleton

class TokenRepositoryImpl @Inject constructor (
    private val local: TokenDataSource.Local
) : TokenRepository {

    override suspend fun getAccessToken(): String? = try {
        Log.d(TAG, "getAccessToken: ${local.getAccessToken().firstOrNull()}")
        local.getAccessToken().firstOrNull()
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }

    override suspend fun getRefreshToken(): String? = try {
        Log.d(TAG, "getRefreshToken: ${local.getRefreshToken().firstOrNull()}")
        local.getRefreshToken().firstOrNull()
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }

    override suspend fun getFcmToken(): String? = try {
        Log.d(TAG, "getFcmToken: ${local.getFcmToken().firstOrNull()}")
        local.getFcmToken().firstOrNull()
    }catch (e: Exception){
        e.printStackTrace()
        null
    }

    override suspend fun saveAccessToken(token: String) {
        try {
            Log.d(TAG, "saveAccessToken: $token")
            local.saveAccessToken(token)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun saveRefreshToken(token: String) {
        try {
            Log.d(TAG, "saveRefreshToken: $token")
            local.saveRefreshToken(token)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun saveFcmToken(fcmToken: String) {
        try {
            Log.d(TAG, "saveFcmToken: $fcmToken")
            local.saveFcmToken(fcmToken)
        }catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun clearAuthTokens() {
        try {
            Log.d(TAG, "clearAuthTokens")
            local.clearAuthTokens()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object{
        private const val TAG = "TokenRepositoryImpl"
    }
}
