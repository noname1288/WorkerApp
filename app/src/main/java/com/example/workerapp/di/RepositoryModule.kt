package com.example.workerapp.di

import com.example.workerapp.data.ChatRepository
import com.example.workerapp.data.ChatbotRepository
import com.example.workerapp.data.JobServiceRepository
import com.example.workerapp.data.NotificationRepository
import com.example.workerapp.data.ReviewRepository
import com.example.workerapp.data.SystemRepository
import com.example.workerapp.data.TokenRepository
import com.example.workerapp.data.UserRepository
import com.example.workerapp.data.repository.ChatRepositoryImpl
import com.example.workerapp.data.repository.ChatbotRepositoryImpl
import com.example.workerapp.data.repository.JobServiceRepositoryImpl
import com.example.workerapp.data.repository.NotificationRepositoryImpl
import com.example.workerapp.data.repository.ReviewRepositoryImpl
import com.example.workerapp.data.repository.SystemRepositoryImpl
import com.example.workerapp.data.repository.TokenRepositoryImpl
import com.example.workerapp.data.repository.UserRepositoryImpl
import com.example.workerapp.data.source.remote.ChatbotRemoteImpl
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

    @Binds
    @Singleton
    abstract fun bindReviewRepository(
        impl: ReviewRepositoryImpl
    ) : ReviewRepository

    @Binds
    @Singleton
    abstract fun bindSystemRepository(
        impl: SystemRepositoryImpl
    ) : SystemRepository

    @Binds
    @Singleton
    abstract fun bindMessageRepository(
        impl: ChatRepositoryImpl
    ) : ChatRepository

    @Binds
    @Singleton
    abstract fun bindChatbotRepository(
        impl: ChatbotRepositoryImpl
    ) : ChatbotRepository
}
