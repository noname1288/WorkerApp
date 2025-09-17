package com.example.workerapp.data.source.local

import com.example.workerapp.data.source.UserDataSource
import com.example.workerapp.data.source.local.room.UserDao
import com.example.workerapp.data.source.local.room.entity.UserLocalEntity
import kotlinx.coroutines.flow.Flow

class UserLocalImpl(private val userDao: UserDao) : UserDataSource.Local {
    override suspend fun getUserProfile(): Flow<UserLocalEntity?> {
        return userDao.getUser()
    }

    override suspend fun saveUserProfile(user: UserLocalEntity) {
        userDao.insertUser(user)
    }

    override suspend fun clearUserProfile() {
        userDao.clearUsers()
    }

    companion object{
        const val TAG = "UserLocalImpl"

        private var singleton: UserLocalImpl? = null
        fun getInstance(userDao: UserDao): UserLocalImpl{
            if(singleton == null){
                singleton = UserLocalImpl(userDao)
            }
            return singleton!!
        }
    }
}
