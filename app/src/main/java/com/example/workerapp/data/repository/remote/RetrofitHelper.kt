package com.example.workerapp.data.repository.remote

import com.example.workerapp.data.repository.remote.api.JobApi
import com.example.workerapp.data.repository.remote.api.UserApi
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitHelper {
    private val baseUrl = "https://bedatn-eosin.vercel.app/api/"

    private val moshi = Moshi.Builder()
        .build()

    private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    val jobApi: JobApi by lazy { retrofit.create(JobApi::class.java) }
    val userApi: UserApi by lazy { retrofit.create(UserApi::class.java) }
}
