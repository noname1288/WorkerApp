package com.example.workerapp.data.source.local

import com.example.workerapp.data.source.UserDataSource
import com.example.workerapp.data.source.local.room.UserDao
import com.example.workerapp.data.source.local.room.entity.UserLocalEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserLocalImpl @Inject constructor(
    private val userDao: UserDao
) : UserDataSource.Local {
    override fun getUserProfile(): Flow<UserLocalEntity?> {
        return userDao.getUser()
    }

    override suspend fun saveUserProfile(user: UserLocalEntity) {
        userDao.insertUser(user)
    }

    override suspend fun updateAvatar(userUid: String, avatarUrl: String) {
        userDao.updateAvatar(userUid, avatarUrl)
    }

    override suspend fun clearUserProfile() {
        userDao.clearUsers()
    }

}
