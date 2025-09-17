package com.example.workerapp.data.source.remote.interceptor

import com.example.workerapp.data.TokenRepository
import com.example.workerapp.utils.annotation.AuthRequired
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.Invocation

// Interceptor để thêm Authorization header
class AuthInterceptor(private val tokenRepository: TokenRepository) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val invocation = chain.request().tag(Invocation::class.java)
        val needAuth = invocation?.method()?.getAnnotation(AuthRequired::class.java) != null

        val newRequest = if (needAuth) {
            val builder = chain.request().newBuilder()

            val token = runBlocking {
                tokenRepository.getAccessToken()
            }

            token?.let {
                builder.addHeader("Authorization", "Bearer $it")
            }

            builder.build()
        } else {
            chain.request()
        }

        return chain.proceed(newRequest)
    }
}
