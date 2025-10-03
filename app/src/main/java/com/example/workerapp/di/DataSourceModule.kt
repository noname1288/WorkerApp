package com.example.workerapp.di

import com.example.workerapp.data.source.JobDataSource
import com.example.workerapp.data.source.JobServiceDataSource
import com.example.workerapp.data.source.NotificationDataSource
import com.example.workerapp.data.source.TokenDataSource
import com.example.workerapp.data.source.UserDataSource
import com.example.workerapp.data.source.local.JobServiceLocalImpl
import com.example.workerapp.data.source.local.TokenLocalImpl
import com.example.workerapp.data.source.local.UserLocalImpl
import com.example.workerapp.data.source.remote.JobRemoteImpl
import com.example.workerapp.data.source.remote.JobServiceRemoteImpl
import com.example.workerapp.data.source.remote.NotificationRemoteImpl
import com.example.workerapp.data.source.remote.UserRemoteImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule{
    /* *
    * Local
    * */
    @Binds
    @Singleton
    abstract fun bindTokenLocal(
        impl: TokenLocalImpl
    ): TokenDataSource.Local

    @Binds
    @Singleton
    abstract fun bindUserLocal(
        impl: UserLocalImpl
    ) : UserDataSource.Local

    @Binds
    @Singleton
    abstract fun bindJobServiceLocal(
        impl: JobServiceLocalImpl
    ) : JobServiceDataSource.Local

    /* *
    * Remote
    * */
    @Binds
    @Singleton
    abstract fun bindUserRemote(
        impl: UserRemoteImpl
    ) : UserDataSource.Remote

    @Binds
    @Singleton
    abstract fun bindJobServiceRemote(
        impl: JobServiceRemoteImpl
    ) : JobServiceDataSource.Remote

    @Binds
    @Singleton
    abstract fun bindJobRemote(
        impl: JobRemoteImpl
    ) : JobDataSource.Remote

    @Binds
    @Singleton
    abstract fun bindNotificationRemote(
        impl: NotificationRemoteImpl
    ) : NotificationDataSource.Remote

}
