package com.example.workerapp.data

interface TokenRepository {
    fun getToken(): String?

    fun saveToken(token: String)

    fun clearToken()
}
