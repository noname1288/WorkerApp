package com.example.workerapp.data

import com.example.workerapp.data.source.local.room.entity.UserLocalEntity
import com.example.workerapp.data.source.remote.dto.request.ChangePasswordRequest
import com.example.workerapp.data.source.remote.dto.request.ForgotPasswordRequest
import com.example.workerapp.data.source.remote.dto.request.ResetPasswordRequest
import com.example.workerapp.data.source.remote.dto.request.UserLoginRequest
import com.example.workerapp.data.source.remote.dto.request.UserLoginWithGGRequest
import com.example.workerapp.data.source.remote.dto.request.UserRegisterRequest
import com.example.workerapp.data.source.remote.dto.request.UserUpdateRequest
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody

interface UserRepository {

    suspend fun login(request: UserLoginRequest): Result<UserLocalEntity>

    suspend fun saveFcmToken(fcmToken: String): Result<Unit>

    suspend fun register(request: UserRegisterRequest): Result<UserLocalEntity>

    suspend fun loginWithGoogle(request: UserLoginWithGGRequest): Result<UserLocalEntity>

    suspend fun changePassword(request: ChangePasswordRequest) : Result<Unit>

    suspend fun sendEmail(request: ForgotPasswordRequest) : Result<String>
    suspend fun resetPassword(request: ResetPasswordRequest) : Result<String>

    fun getUserProfile(): Flow<UserLocalEntity?>

    suspend fun saveUserProfile(user: UserLocalEntity)

    suspend fun updateProfile(request: UserUpdateRequest): Result<UserLocalEntity>

    suspend fun uploadImage(userUid: String, imagePart: MultipartBody.Part) : Result<String>

    suspend fun clearUserProfile()

}
