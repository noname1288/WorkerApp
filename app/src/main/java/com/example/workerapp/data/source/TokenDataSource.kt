package com.example.workerapp.data.source

import kotlinx.coroutines.flow.Flow

interface TokenDataSource {
    /* *
    * Local
    * */
    interface Local{
        fun getAccessToken(): Flow<String?>

        fun getRefreshToken(): Flow<String?>

        suspend fun saveAccessToken(accessToken: String)

        suspend fun saveRefreshToken(refreshToken: String)

        suspend fun clearTokens()
    }

    /* *
    * Remote
    * */
}
