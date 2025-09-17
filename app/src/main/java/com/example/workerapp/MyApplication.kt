package com.example.workerapp

import android.app.Application
import com.example.workerapp.data.TokenRepository
import com.example.workerapp.data.UserRepository
import com.example.workerapp.data.repository.TokenRepositoryImpl
import com.example.workerapp.data.repository.UserRepositoryImpl
import com.example.workerapp.data.source.local.TokenLocalImpl
import com.example.workerapp.data.source.local.UserLocalImpl
import com.example.workerapp.data.source.local.room.AppDatabase
import com.example.workerapp.data.source.remote.RetrofitHelper
import com.example.workerapp.data.source.remote.UserRemoteImpl
import com.example.workerapp.data.source.remote.api.JobApi
import com.example.workerapp.data.source.remote.api.ServiceApi
import com.example.workerapp.data.source.remote.api.UserApi
import com.example.workerapp.di.dataStore

class MyApplication : Application() {
    lateinit var tokenRepository: TokenRepository
        private set

    lateinit var userRepository: UserRepository
        private set

    lateinit var database : AppDatabase

    override fun onCreate() {
        super.onCreate()
        //room database
        database = AppDatabase.getDatabase(this)

        //repository
        val dataStore = this.dataStore
        val tokenLocalImpl = TokenLocalImpl(dataStore)
        tokenRepository = TokenRepositoryImpl(tokenLocalImpl)

        // Retrofit
        RetrofitHelper.init(tokenRepository)

        userRepository = UserRepositoryImpl(
            local = UserLocalImpl.getInstance(database.userDao()),
            remote = UserRemoteImpl.getInstance(),
            tokenRepository = tokenRepository

        )


    }
}