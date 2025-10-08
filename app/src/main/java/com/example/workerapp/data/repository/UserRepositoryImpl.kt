package com.example.workerapp.data.repository

import android.util.Log
import com.example.workerapp.data.TokenRepository
import com.example.workerapp.data.UserRepository
import com.example.workerapp.data.source.UserDataSource
import com.example.workerapp.data.source.local.room.entity.UserLocalEntity
import com.example.workerapp.data.source.remote.dto.NetworkResult
import com.example.workerapp.data.source.remote.dto.request.UserLoginRequest
import com.example.workerapp.data.source.remote.dto.request.UserLoginWithGGRequest
import com.example.workerapp.data.source.remote.dto.request.UserRegisterRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val local: UserDataSource.Local,
    private val remote: UserDataSource.Remote,
    private val tokenRepository: TokenRepository
) : UserRepository {
    override suspend fun login(request: UserLoginRequest): Result<UserLocalEntity> {
        try {
            val response = remote.login(request)
            when (response) {
                is NetworkResult.Error -> {
                    return Result.failure(Exception(response.message))
                }

                is NetworkResult.Success -> {
                    val user = response.data.user
                    val accessToken = response.data.token
                    val refreshToken = response.data.refreshToken

                    //save token to data store
                    tokenRepository.saveAccessToken(accessToken)
                    tokenRepository.saveRefreshToken(refreshToken)


                    //Map UserWrapperResponse to UserLocalEntity
                    val userLocal = UserLocalEntity(
                        uid = user.uid,
                        username = user.username,
                        gender = user.gender,
                        dob = user.dob,
                        avatar = user.avatar,
                        tel = user.tel,
                        location = user.location,
                        email = user.email,
                        provider = user.provider,
                        emailVerified = user.emailVerified,
                        requiresProfileUpdate = user.requiresProfileUpdate,
                        role = user.role,
                        lastLogin = user.lastLogin
                    )

                    try {
                        //clear old user
                        local.clearUserProfile()
                        //save user to local database
                        local.saveUserProfile(userLocal)
                        Log.d(TAG, "login - save successfully to local database")
                        return Result.success(userLocal)
                    } catch (dbException: Exception) {
                        Log.e(
                            TAG,
                            "login - Room Exception: error to save user's profile to local ${dbException.message}"
                        )
                        return Result.failure(dbException)
                    }
                }
            }

        } catch (e: Exception) {
            Log.e(TAG, "login - Exception: ${e.message}")
            return Result.failure(e)
        }
    }

    override suspend fun saveFcmToken(fcmToken: String): Result<Unit> {
        try {
            val response = remote.updateFcmToken(fcmToken)
            when (response) {
                is NetworkResult.Error -> {
                    Log.e(TAG, "saveFcmToken - Error: ${response.message}")
                    return Result.failure(Exception(response.message))
                }

                is NetworkResult.Success -> {
                    Log.d(TAG, "saveFcmToken - FCM token updated successfully on server")
                    return Result.success(Unit)
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "saveFcmToken - Exception: ${e.message}")
            return Result.failure(e)
        }
    }

    override suspend fun register(request: UserRegisterRequest): Result<UserLocalEntity> {
        try {
            val response = remote.register(request)

            when (response) {
                is NetworkResult.Error -> {
                    return Result.failure(Exception(response.message))
                }

                is NetworkResult.Success -> {
                    val user = response.data.user
                    val token = response.data.token

                    //save token to data store
                    tokenRepository.saveAccessToken(token)

                    //Map UserWrapperResponse to UserLocalEntity
                    val userLocal = UserLocalEntity(
                        uid = user.uid,
                        username = user.username,
                        gender = user.gender,
                        dob = user.dob,
                        avatar = user.avatar,
                        tel = user.tel,
                        location = user.location,
                        email = user.email,
                        provider = user.provider,
                        emailVerified = user.emailVerified,
                        requiresProfileUpdate = user.requiresProfileUpdate,
                        role = user.role,
                        lastLogin = user.lastLogin
                    )

                    try {
                        //clear old user
                        local.clearUserProfile()
                        //save user to local database
                        local.saveUserProfile(userLocal)
                        Log.d(TAG, "register - save successfully to local database")
                        return Result.success(userLocal)
                    } catch (dbException: Exception) {
                        Log.e(
                            TAG,
                            "register - Room Exception: error to save user's profile to local ${dbException.message}"
                        )
                        return Result.failure(dbException)
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "register - Exception: ${e.message}")
            return Result.failure(e)
        }
    }

    override suspend fun loginWithGoogle(request: UserLoginWithGGRequest): Result<UserLocalEntity> {
        try {
            val response = remote.loginWithGoogle(request)

            when (response) {
                is NetworkResult.Error -> {
                    return Result.failure(Exception(response.message))
                }

                is NetworkResult.Success -> {
                    val user = response.data.user
                    val token = response.data.token

                    //save token to data store
                    tokenRepository.saveAccessToken(token)

                    //Map UserWrapperResponse to UserLocalEntity
                    val userLocal = UserLocalEntity(
                        uid = user.uid,
                        username = user.username,
                        gender = user.gender,
                        dob = user.dob,
                        avatar = user.avatar,
                        tel = user.tel,
                        location = user.location,
                        email = user.email,
                        provider = user.provider,
                        emailVerified = user.emailVerified,
                        requiresProfileUpdate = user.requiresProfileUpdate,
                        role = user.role,
                        lastLogin = user.lastLogin
                    )

                    try {
                        //clear old user
                        local.clearUserProfile()
                        //save user to local database
                        local.saveUserProfile(userLocal)
                        Log.d(TAG, "loginWithGG - save successfully to local database")
                        return Result.success(userLocal)
                    } catch (dbException: Exception) {
                        Log.e(
                            TAG,
                            "loginWithGG - Room Exception: error to save user's profile to local ${dbException.message}"
                        )
                        return Result.failure(dbException)
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "loginWithGoogle - Exception: ${e.message}")
            return Result.failure(e)
        }
    }

    override suspend fun getUserProfile(): Flow<Result<UserLocalEntity?>> {
        return local.getUserProfile()
            .map { user ->
                Result.success(user)
            }
            .catch { e ->
                emit(Result.failure(e))
            }
    }

    override suspend fun saveUserProfile(user: UserLocalEntity) {
        local.saveUserProfile(user)

    }

    override suspend fun clearUserProfile() {
        local.clearUserProfile()
    }

    companion object {
        private val TAG = "UserRepositoryImpl"
    }
}

