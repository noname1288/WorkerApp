package com.example.workerapp.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.workerapp.data.source.local.datastore.PrefKeys
import com.example.workerapp.data.source.local.room.AppDatabase
import com.example.workerapp.utils.Constant
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

//property delegate
val Context.dataStore by preferencesDataStore(name = PrefKeys.PREFS_NAME)

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL(
                """
            CREATE TABLE IF NOT EXISTS application_history (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                applicationId TEXT NOT NULL,
                jobId TEXT NOT NULL,
                createdAt TEXT NOT NULL,
                serviceType TEXT NOT NULL
            )
            """.trimIndent()
            )
        }
    }

    val MIGRATION_2_3 = object : Migration(2, 3) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE application_history ADD COLUMN status TEXT NOT NULL DEFAULT ''")
        }
    }

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            Constant.DATABASE_NAME
        )
            .addMigrations(MIGRATION_1_2)
            .addMigrations(MIGRATION_2_3)
            .build()
    }

    @Provides
    fun provideUserDao(database: AppDatabase) = database.userDao()

    @Provides
    fun provideServiceDao(database: AppDatabase) = database.serviceDao()

    @Provides
    fun provideNotificationDao(database: AppDatabase) = database.notificationDao()

    @Provides
    fun provideJobDao(database: AppDatabase) = database.applicationDao()
}

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    @Singleton
    fun providePreferencesDataStore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> = context.dataStore
}

