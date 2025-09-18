package com.example.workerapp.data

import com.example.workerapp.data.source.local.room.entity.UserLocalEntity
import com.example.workerapp.data.source.remote.dto.request.UserLoginRequest
import com.example.workerapp.data.source.remote.dto.request.UserLoginWithGGRequest
import com.example.workerapp.data.source.remote.dto.request.UserRegisterRequest
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    suspend fun login(request: UserLoginRequest): Result<UserLocalEntity>

    suspend fun saveFcmToken(fcmToken: String): Result<Unit>

    suspend fun register(request: UserRegisterRequest): Result<UserLocalEntity>

    suspend fun loginWithGoogle(request: UserLoginWithGGRequest): Result<UserLocalEntity>

    suspend fun getUserProfile(): Flow<Result<UserLocalEntity?>>

    suspend fun saveUserProfile(user: UserLocalEntity)

    suspend fun clearUserProfile()
}
