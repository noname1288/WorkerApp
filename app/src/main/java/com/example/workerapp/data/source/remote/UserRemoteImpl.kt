package com.example.workerapp.data.source.remote

import android.util.Log
import com.example.workerapp.data.source.UserDataSource
import com.example.workerapp.data.source.model.base.UserModel
import com.example.workerapp.data.source.remote.api.UserApi
import com.example.workerapp.data.source.remote.dto.NetworkResult
import com.example.workerapp.data.source.remote.dto.request.ChangePasswordRequest
import com.example.workerapp.data.source.remote.dto.request.FcmTokenRequest
import com.example.workerapp.data.source.remote.dto.request.ForgotPasswordRequest
import com.example.workerapp.data.source.remote.dto.request.UserLoginRequest
import com.example.workerapp.data.source.remote.dto.request.UserLoginWithGGRequest
import com.example.workerapp.data.source.remote.dto.request.UserRegisterRequest
import com.example.workerapp.data.source.remote.dto.request.UserUpdateRequest
import com.example.workerapp.data.source.remote.dto.response.ApiErrorResponse
import com.example.workerapp.data.source.remote.dto.response.UserWrapperResponse
import com.squareup.moshi.Moshi
import okhttp3.MultipartBody
import javax.inject.Inject

class UserRemoteImpl @Inject constructor(
    private val userApi: UserApi,
    moshi: Moshi
) : UserDataSource.Remote {
    private val errorAdapter = moshi.adapter(ApiErrorResponse::class.java)

    override suspend fun login(request: UserLoginRequest): NetworkResult<UserWrapperResponse> {
        val response = userApi.login(request)

        return if (response.isSuccessful) {
            val body = response.body()

            if (body != null && body.success) {
                Log.d(TAG, "login: ${body.data}")
                NetworkResult.Success(body.data)
            } else {
                Log.e(TAG, "login error: ${body?.message}")
                NetworkResult.Error(body?.message ?: "Unknown error")
            }

        } else {
            val errorMessage = response.errorBody()?.string()
                ?.let { json -> errorAdapter.fromJson(json)?.error }
                ?: response.message()
                ?: "Request failed with status code ${response.code()}"

            Log.e(TAG, "login Exception: $errorMessage")
            NetworkResult.Error(errorMessage)
        }
    }

    override suspend fun updateFcmToken(fcmToken: String): NetworkResult<Unit> {
        try {
            val response = userApi.updateFcmToken(FcmTokenRequest(fcmToken))

            if (response.success) {
                Log.d(TAG, "updateFcmToken: success")
                return NetworkResult.Success(Unit)
            } else {
                Log.e(TAG, "updateFcmToken error: ${response.message}")
                return NetworkResult.Error(response.message)
            }
        } catch (e: Exception) {
            Log.e(TAG, "updateFcmToken Exception: ${e.message}")
            return NetworkResult.Error(e.message ?: "An unknown error occurred")
        }
    }

    override suspend fun register(request: UserRegisterRequest): NetworkResult<UserWrapperResponse> {
        try {
            val response = userApi.register(request)

            if (response.success) {
                Log.d(TAG, "register: ${response.data}")
                return NetworkResult.Success(response.data)
            } else {
                Log.e(TAG, "register error: ${response.message}")
                return NetworkResult.Error(response.message)
            }
        } catch (e: Exception) {
            Log.e(TAG, "register: ${e.message}")
            return NetworkResult.Error(e.message ?: "An unknown error occurred")
        }
    }

    override suspend fun loginWithGoogle(request: UserLoginWithGGRequest): NetworkResult<UserWrapperResponse> {
        return try {
            val response = userApi.loginWithGoogle(request)

            if (response.success) {
                Log.d(TAG, "login with GG: ${response.data}")
                NetworkResult.Success(response.data)
            } else {
                Log.e(TAG, "login with GG error: ${response.message}")
                NetworkResult.Error(response.message)
            }
        } catch (e: Exception) {
            Log.e(TAG, "login with GG Exception: ${e.message}")
            NetworkResult.Error(e.message ?: "An unknown error occurred")
        }
    }

    override suspend fun changePassword(request: ChangePasswordRequest): NetworkResult<Unit> {
        val response = userApi.changePassword(request)

        return if (response.isSuccessful) {
            val body = response.body()

            if (body != null && body.success) {
                Log.d(TAG, "changePassword: success")
                NetworkResult.Success(Unit)
            } else {
                Log.e(TAG, "changePassword error: ${body?.message}")
                NetworkResult.Error(body?.message ?: "Unknown error")
            }
        } else {
            val errorMessage = response.errorBody()?.string()
                ?.let { json -> errorAdapter.fromJson(json)?.error }
                ?: response.message()
                ?: "Request failed with status code ${response.code()}"

            Log.e(TAG, "changePassword Exception: $errorMessage")
            NetworkResult.Error(errorMessage)
        }
    }

    override suspend fun sendEmail(request: ForgotPasswordRequest): NetworkResult<Unit> {
        val response = userApi.sendEmail(request)

        if (response.isSuccessful) {
            val body = response.body()

            if (body != null && body.success) {
                Log.d(TAG, "sendEmail: success")
                return NetworkResult.Success(Unit)
            } else {
                Log.e(TAG, "sendEmail error: ${body?.message}")
                return NetworkResult.Error(body?.message ?: "Unknown error")
            }
        } else {
            val errorMessage = response.errorBody()?.string()
                ?.let { json -> errorAdapter.fromJson(json)?.error }
                ?: response.message()
                ?: "Request failed with status code ${response.code()}"

            Log.e(TAG, "sendEmail Exception: $errorMessage")
            return NetworkResult.Error(errorMessage)
        }
    }

    override suspend fun uploadImage(imagePart: MultipartBody.Part): NetworkResult<String> {
        val response = userApi.uploadImage(imagePart)

        return if (response.isSuccessful) {
            val body = response.body()

            if (body != null && body.success) {
                Log.d(TAG, "uploadImage: ${body.url}")
                NetworkResult.Success(body.url)
            } else {
                Log.e(TAG, "uploadImage error: ${body?.message}")
                NetworkResult.Error(body?.message ?: "Unknown error")
            }
        } else {
            val errorMessage = response.errorBody()?.string()
                ?.let { json -> errorAdapter.fromJson(json)?.error }
                ?: response.message()
                ?: "Request failed with status code ${response.code()}"

            Log.e(TAG, "uploadImage Exception: $errorMessage")
            NetworkResult.Error(errorMessage)
        }
    }

    override suspend fun updateProfile(request: UserUpdateRequest): NetworkResult<UserModel> {
        val response = userApi.updateProfile(request)

        if (response.isSuccessful) {
            val body = response.body()

            if (body != null && body.success) {
                Log.d(TAG, "update successfully: ${body.user}")
                return NetworkResult.Success(body.user)
            } else {
                Log.e(TAG, "update error: ${body?.message}")
                return NetworkResult.Error(body?.message ?: "Unknown error")
            }
        } else {
            val errorMessage = response.errorBody()?.string()
                ?.let { json -> errorAdapter.fromJson(json)?.error }
                ?: response.message()
                ?: "Request failed with status code ${response.code()}"

            Log.e(TAG, "update Exception: $errorMessage")
            return NetworkResult.Error(errorMessage)
        }
    }

    companion object {
        const val TAG = "UserRemoteImpl"
    }
}
