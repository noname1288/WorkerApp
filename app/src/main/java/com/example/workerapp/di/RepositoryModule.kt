package com.example.workerapp.di

import com.example.workerapp.data.JobServiceRepository
import com.example.workerapp.data.NotificationRepository
import com.example.workerapp.data.TokenRepository
import com.example.workerapp.data.UserRepository
import com.example.workerapp.data.repository.JobServiceRepositoryImpl
import com.example.workerapp.data.repository.NotificationRepositoryImpl
import com.example.workerapp.data.repository.TokenRepositoryImpl
import com.example.workerapp.data.repository.UserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule{

    @Binds
    @Singleton
    abstract fun bindTokenRepository(
        impl: TokenRepositoryImpl
    ) : TokenRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        impl: UserRepositoryImpl
    ) : UserRepository

    @Binds
    @Singleton
    abstract fun bindJobServiceRepository(
        impl: JobServiceRepositoryImpl
    ) : JobServiceRepository

    @Binds
    @Singleton
    abstract fun bindNotificationRepository(
        impl: NotificationRepositoryImpl
    ) : NotificationRepository

}
