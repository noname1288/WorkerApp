package com.example.workerapp.data.source.remote.api

import com.example.workerapp.data.source.remote.dto.BaseResponse
import com.example.workerapp.data.source.remote.dto.TokenResponse
import com.example.workerapp.data.source.remote.dto.request.ChangePasswordRequest
import com.example.workerapp.data.source.remote.dto.request.FcmTokenRequest
import com.example.workerapp.data.source.remote.dto.request.ForgotPasswordRequest
import com.example.workerapp.data.source.remote.dto.request.RefreshTokenRequest
import com.example.workerapp.data.source.remote.dto.request.UserLoginRequest
import com.example.workerapp.data.source.remote.dto.request.UserLoginWithGGRequest
import com.example.workerapp.data.source.remote.dto.request.UserRegisterRequest
import com.example.workerapp.data.source.remote.dto.request.UserUpdateRequest
import com.example.workerapp.data.source.remote.dto.response.ChangePasswordResponse
import com.example.workerapp.data.source.remote.dto.response.ForgotPasswordResponse
import com.example.workerapp.data.source.remote.dto.response.TokenWrapperResponse
import com.example.workerapp.data.source.remote.dto.response.UploadingImageResponse
import com.example.workerapp.data.source.remote.dto.response.UserUpdateResponse
import com.example.workerapp.data.source.remote.dto.response.UserWrapperResponse
import com.example.workerapp.utils.annotation.AuthRequired
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part

interface UserApi {
    @POST("auth/me")
    suspend fun login(@Body request: UserLoginRequest): Response<BaseResponse<UserWrapperResponse>>

    @POST("auth/create")
    suspend fun register(@Body request: UserRegisterRequest): BaseResponse<UserWrapperResponse>

    @POST("auth/loginGG")
    suspend fun loginWithGoogle(@Body request: UserLoginWithGGRequest): BaseResponse<UserWrapperResponse>

    @POST("auth/client/refreshToken")
    suspend fun refreshToken(@Body request: RefreshTokenRequest): BaseResponse<TokenWrapperResponse>

    @AuthRequired
    @PUT("users/change-password")
    suspend fun changePassword(@Body request: ChangePasswordRequest) : Response<ChangePasswordResponse>

    @POST("emails/send")
    suspend fun sendEmail(@Body request: ForgotPasswordRequest) : Response<ForgotPasswordResponse>

    @AuthRequired
    @POST("devices")
    suspend fun updateFcmToken(
        @Body request: FcmTokenRequest
    ): TokenResponse<Any>

    @Multipart
    @POST("images/upload")
    suspend fun uploadImage(
        @Part image: MultipartBody.Part
    ): Response<UploadingImageResponse>

    @AuthRequired
    @POST("users/update")
    suspend fun updateProfile(
        @Body request: UserUpdateRequest
    ): Response<UserUpdateResponse>

}
