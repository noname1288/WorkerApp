package com.example.workerapp.data.repository.remote.repository

import android.util.Log
import com.example.workerapp.data.repository.remote.NetworkResult
import com.example.workerapp.data.repository.remote.RetrofitHelper
import com.example.workerapp.data.repository.remote.api.UserApi
import com.example.workerapp.data.repository.remote.dto.request.UserLoginRequest
import com.example.workerapp.data.repository.remote.dto.request.UserRegisterRequest
import com.example.workerapp.data.repository.remote.dto.response.UserWrapperResponse

class UserRepositoryImpl(private val userApi: UserApi) : UserRepository {
    override suspend fun login(request: UserLoginRequest): NetworkResult<UserWrapperResponse> {
        try {
            val response = userApi.login(request)

            if (response.success) {
                Log.d(TAG, "login: ${response.data}")
                return NetworkResult.Success(response.data ?: UserWrapperResponse())
            } else {
                Log.e(TAG, "login error: ${response.message}")
                return NetworkResult.Error(response.message)
            }
        } catch (e: Exception) {
            Log.e(TAG, "login: ${e.message}")
            return NetworkResult.Error(e.message ?: "An unknown error occurred")
        }
    }

    override suspend fun register(request: UserRegisterRequest): NetworkResult<UserWrapperResponse> {
        try {
            val response = userApi.register(request)

            if (response.success) {
                Log.d(TAG, "register: ${response.data}")
                return NetworkResult.Success(response.data ?: UserWrapperResponse())
            } else {
                Log.e(TAG, "register error: ${response.message}")
                return NetworkResult.Error(response.message)
            }
        } catch (e: kotlin.Exception) {
            Log.e(TAG, "register: ${e.message}")
            return NetworkResult.Error(e.message ?: "An unknown error occurred")
        }
    }

    companion object {
        const val TAG = "UserRepositoryImpl"

        var singleton: UserRepositoryImpl? = null

        fun getInstance(): UserRepositoryImpl {
            return singleton ?: UserRepositoryImpl(RetrofitHelper.userApi).also { singleton = it }
        }
    }
}
