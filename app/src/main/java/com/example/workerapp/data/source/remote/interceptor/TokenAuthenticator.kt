package com.example.workerapp.data.source.remote.interceptor

import com.example.workerapp.data.TokenRepository
import com.example.workerapp.data.source.remote.api.UserApi
import com.example.workerapp.data.source.remote.dto.request.RefreshTokenRequest
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject
import javax.inject.Provider

class TokenAuthenticator @Inject constructor(
    private val tokenRepository: TokenRepository,
    private val userApiProvider: Provider<UserApi>
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        // if we've already attempted to authenticate 3 times, give up
        if (responseCount(response) >= 2) {
            return null
        }

        val refreshResponse = runBlocking {
            val refreshToken = tokenRepository.getRefreshToken()

            if (refreshToken.isNullOrEmpty())
                throw RuntimeException("Refresh token is null or empty")

            try {
                userApiProvider.get().refreshToken(RefreshTokenRequest(refreshToken))
            } catch (e: Exception) {
                null
            }
        }

        if ( refreshResponse == null || !refreshResponse.success) return null

        val newToken = refreshResponse.data.idToken

        runBlocking {
            tokenRepository.saveAccessToken(newToken)
        }

        // use new access token to send request again
        return response.request.newBuilder()
            .header("Authorization", "Bearer $newToken")
            .build()
    }

    private fun responseCount(response: Response): Int {
        var count = 1
        var prior = response.priorResponse
        while (prior != null) {
            count++
            prior = prior.priorResponse
        }

        return count
    }
}
