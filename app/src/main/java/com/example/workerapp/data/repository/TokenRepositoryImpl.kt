package com.example.workerapp.data.repository

import com.example.workerapp.data.TokenRepository
import com.example.workerapp.data.source.TokenDataSource
import kotlinx.coroutines.flow.firstOrNull

class TokenRepositoryImpl (
    private val local: TokenDataSource.Local
) : TokenRepository {

    override suspend fun getAccessToken(): String? = try {
        local.getAccessToken().firstOrNull()
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }

    override suspend fun getRefreshToken(): String? = try {
        local.getRefreshToken().firstOrNull()
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }

    override suspend fun saveAccessToken(token: String) {
        try {
            local.saveAccessToken(token)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun saveRefreshToken(token: String) {
        try {
            local.saveRefreshToken(token)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun clearTokens() {
        try {
            local.clearTokens()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}