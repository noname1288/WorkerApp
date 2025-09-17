package com.example.workerapp.data.source.remote.interceptor

import com.example.workerapp.data.source.local.TokenLocalImpl
import com.example.workerapp.data.source.remote.api.UserApi
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class TokenAuthenticator(
    private val tokenLocalImpl: TokenLocalImpl,
    private val userApi: UserApi
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        // if we've already attempted to authenticate 3 times, give up
        if (responseCount(response) >= 2) {
            return null
        }

        // get the refresh token from DataStore
        // call refresh token api
        val refreshReponse = runBlocking {
            try {
                //userApi.refreshToken("Bearer ${tokenLocalImpl.getRefreshToken()}")
            } catch (e: Exception) {
                null
            }
        }

//        if (!refreshReponse.success) return null

        runBlocking {
            // save new token to DataStore
        }

        // use new access token to send request again

        return null
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
