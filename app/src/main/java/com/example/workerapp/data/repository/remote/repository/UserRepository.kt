package com.example.workerapp.data.repository.remote.repository

import com.example.workerapp.data.model.base.UserModel
import com.example.workerapp.data.repository.remote.NetworkResult
import com.example.workerapp.data.repository.remote.dto.BaseUserResponse
import com.example.workerapp.data.repository.remote.dto.request.UserLoginRequest
import com.example.workerapp.data.repository.remote.dto.request.UserRegisterRequest
import com.example.workerapp.data.repository.remote.dto.response.UserWrapperResponse

interface UserRepository {
    suspend fun login(request: UserLoginRequest): NetworkResult<UserWrapperResponse>
    suspend fun register(request: UserRegisterRequest) : NetworkResult<UserWrapperResponse>
}