package com.example.workerapp.data.source.remote.interceptor

import com.example.workerapp.utils.annotation.AuthRequired
import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.Invocation

// Interceptor để thêm Authorization header
class AuthInterceptor(private val tokenProvider: () -> String?) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val invocation = chain.request().tag(Invocation::class.java)
        val needAuth = invocation?.method()?.getAnnotation(AuthRequired::class.java) != null

        val newRequest = if (needAuth) {
            val builder = chain.request().newBuilder()
            tokenProvider()?.let { token ->
                builder.addHeader("Authorization", "Bearer $token")
            }
            builder.build()
        } else {
            chain.request()
        }

        return chain.proceed(newRequest)
    }
}
