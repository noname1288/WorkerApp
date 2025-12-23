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

    @Query("DELETE FROM application_history WHERE applicationId = :applicationId")
    suspend fun deleteByApplicationId(applicationId : String)

    /**
     * Lấy danh sách application theo jobId
     * Sắp xếp theo thời gian tạo mới nhất (giảm dần)
     */
    @Query("""
    SELECT * FROM application_history
    WHERE jobId = :jobId
    ORDER BY createdAt DESC
""")
    suspend fun getApplicationsByJobId(
        jobId: String
    ): List<ApplicationModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListApplications(
        applications: List<ApplicationModel>
    ): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertApplication(
        application: ApplicationModel
    ): Long

}
