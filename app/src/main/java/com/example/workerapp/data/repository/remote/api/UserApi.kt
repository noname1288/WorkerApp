package com.example.workerapp.data.repository.remote.api

import com.example.workerapp.data.repository.remote.NetworkResult
import com.example.workerapp.data.repository.remote.dto.BaseUserResponse
import com.example.workerapp.data.repository.remote.dto.request.UserLoginRequest
import com.example.workerapp.data.repository.remote.dto.request.UserLoginWithGGRequest
import com.example.workerapp.data.repository.remote.dto.request.UserRegisterRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApi {
    @POST("users/me")
    suspend fun login(@Body request: UserLoginRequest): BaseUserResponse

    @POST("users/create")
    suspend fun register(@Body request: UserRegisterRequest): BaseUserResponse

    @POST("users/loginGG")
    suspend fun loginWithGoogle(@Body request: UserLoginWithGGRequest) : BaseUserResponse
}
