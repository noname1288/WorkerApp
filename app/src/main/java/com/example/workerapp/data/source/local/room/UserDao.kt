package com.example.workerapp.data.source.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.workerapp.data.source.local.room.entity.UserLocalEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserLocalEntity)

    @Query("SELECT * FROM users LIMIT 1")
    fun getUser(): Flow<UserLocalEntity?>

    @Query("DELETE FROM users")
    suspend fun clearUsers()
}