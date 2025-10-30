package com.example.workerapp.session

import com.example.workerapp.data.TokenRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor(
    private val tokenRepository: TokenRepository
) {
    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _tokenExpired = MutableSharedFlow<Unit>()
    val tokenExpired: SharedFlow<Unit> = _tokenExpired.asSharedFlow()

    suspend fun checkLoginStatus() {
        val token = tokenRepository.getAccessToken()
        _isLoggedIn.value = token != null
    }


    suspend fun logout() {
        tokenRepository.clearAuthTokens()
        _isLoggedIn.value = false
    }

    suspend fun notifyTokenExpired() {
        tokenRepository.clearAuthTokens()
        _isLoggedIn.value = false
        _tokenExpired.emit(Unit)
    }
}