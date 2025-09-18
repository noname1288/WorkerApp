package com.example.workerapp.data.source

import com.example.workerapp.data.source.local.room.entity.UserLocalEntity
import com.example.workerapp.data.source.remote.dto.NetworkResult
import com.example.workerapp.data.source.remote.dto.request.UserLoginRequest
import com.example.workerapp.data.source.remote.dto.request.UserLoginWithGGRequest
import com.example.workerapp.data.source.remote.dto.request.UserRegisterRequest
import com.example.workerapp.data.source.remote.dto.response.UserWrapperResponse
import kotlinx.coroutines.flow.Flow

interface UserDataSource {
    /* *
    * Local
    * */
    interface Local {
        suspend fun getUserProfile(): Flow<UserLocalEntity?>

        suspend fun saveUserProfile(user: UserLocalEntity)

        suspend fun clearUserProfile()
    }

    /* *
    * Remote
    * */
    interface Remote {
        suspend fun login(request: UserLoginRequest): NetworkResult<UserWrapperResponse>

        suspend fun updateFcmToken(fcmToken: String): NetworkResult<Unit>

        suspend fun register(request: UserRegisterRequest): NetworkResult<UserWrapperResponse>

        suspend fun loginWithGoogle(request: UserLoginWithGGRequest): NetworkResult<UserWrapperResponse>
    }
}
