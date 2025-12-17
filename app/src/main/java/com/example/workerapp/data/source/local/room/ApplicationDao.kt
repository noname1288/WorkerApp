package com.example.workerapp.data.source.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.workerapp.data.source.local.room.entity.ApplicationModel
import kotlinx.coroutines.flow.Flow

@Dao
interface ApplicationDao {

    /**
     * Lấy toàn bộ application history
     * Sắp xếp theo thời gian tạo mới nhất trước
     */
    @Query("""
        SELECT * FROM application_history
        ORDER BY createdAt DESC
    """)
    fun getApplications(): List<ApplicationModel>

    /**
     * Xoá toàn bộ application history
     */
    @Query("DELETE FROM application_history")
    suspend fun clearAll()

    /**
     * Lấy application theo jobId
     * Trả về null nếu không tồn tại
     */
    @Query("""
        SELECT * FROM application_history
        WHERE jobId = :jobId
        LIMIT 1
    """)
    suspend fun getApplicationByJobId(
        jobId: String
    ): ApplicationModel?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListApplications(
        applications: List<ApplicationModel>
    ): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertApplication(
        application: ApplicationModel
    ): Long

}
