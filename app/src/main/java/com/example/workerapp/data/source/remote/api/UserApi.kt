package com.example.workerapp.data.source.remote.api

import com.example.workerapp.data.source.remote.dto.BaseResponse
import com.example.workerapp.data.source.remote.dto.TokenResponse
import com.example.workerapp.data.source.remote.dto.request.FcmTokenRequest
import com.example.workerapp.data.source.remote.dto.request.RefreshTokenRequest
import com.example.workerapp.data.source.remote.dto.request.UserLoginRequest
import com.example.workerapp.data.source.remote.dto.request.UserLoginWithGGRequest
import com.example.workerapp.data.source.remote.dto.request.UserRegisterRequest
import com.example.workerapp.data.source.remote.dto.response.TokenWrapperResponse
import com.example.workerapp.data.source.remote.dto.response.UserWrapperResponse
import com.example.workerapp.utils.annotation.AuthRequired
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface UserApi {
    @POST("auth/me")
    suspend fun login(@Body request: UserLoginRequest): BaseResponse<UserWrapperResponse>

    @POST("auth/create")
    suspend fun register(@Body request: UserRegisterRequest): BaseResponse<UserWrapperResponse>

    @POST("auth/loginGG")
    suspend fun loginWithGoogle(@Body request: UserLoginWithGGRequest): BaseResponse<UserWrapperResponse>

    @POST("auth/client/refreshToken")
    suspend fun refreshToken(@Body request: RefreshTokenRequest): BaseResponse<TokenWrapperResponse>

    @AuthRequired
    @POST("devices")
    suspend fun updateFcmToken(
        @Body request: FcmTokenRequest
    ): TokenResponse<Any>

}
