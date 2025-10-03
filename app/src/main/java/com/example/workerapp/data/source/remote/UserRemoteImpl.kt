package com.example.workerapp.data.source.remote

import android.util.Log
import com.example.workerapp.data.source.UserDataSource
import com.example.workerapp.data.source.remote.api.UserApi
import com.example.workerapp.data.source.remote.dto.NetworkResult
import com.example.workerapp.data.source.remote.dto.request.FcmTokenRequest
import com.example.workerapp.data.source.remote.dto.request.UserLoginRequest
import com.example.workerapp.data.source.remote.dto.request.UserLoginWithGGRequest
import com.example.workerapp.data.source.remote.dto.request.UserRegisterRequest
import com.example.workerapp.data.source.remote.dto.response.UserWrapperResponse
import javax.inject.Inject

class UserRemoteImpl @Inject constructor(
    private val userApi: UserApi
) : UserDataSource.Remote {
    override suspend fun login(request: UserLoginRequest): NetworkResult<UserWrapperResponse> {
        try {
            val response = userApi.login(request)

            if (response.success) {
                Log.d(TAG, "login: ${response.data}")
                return NetworkResult.Success(response.data)
            } else {
                Log.e(TAG, "login error: ${response.message}")
                return NetworkResult.Error(response.message)
            }
        } catch (e: Exception) {
            Log.e(TAG, "login: ${e.message}")
            return NetworkResult.Error(e.message ?: "An unknown error occurred")
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

    companion object {
        const val TAG = "UserRemoteImpl"
    }
}
