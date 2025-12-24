package com.example.workerapp.data.source.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.workerapp.data.source.local.room.entity.ApplicationModel

@Dao
interface ApplicationDao {

    /**
     * get data from table 'application_history'
     * sorted by createdAt (DESC)
     */
    @Query(
        """
        SELECT * FROM application_history
        ORDER BY createdAt DESC
    """
    )
    fun getApplications(): List<ApplicationModel>

    /**
     * delete data from table 'application_history'
     */
    @Query("DELETE FROM application_history")
    suspend fun clearAll()

    /**
     * delete a data following applicationId
     */
    @Query("DELETE FROM application_history WHERE applicationId = :applicationId")
    suspend fun deleteByApplicationId(applicationId: String)

    /**
     * get list data following jobId
     * return empty list [] if it have no data
     */
    @Query(
        """
        SELECT * FROM application_history
        WHERE jobId = :jobId
        ORDER BY createdAt DESC
    """
    )
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

    @Query("""
        UPDATE application_history
        SET status = :newStatus
        WHERE applicationId = :applicationId
    """)
    suspend fun updateStatusByApplicationId(
        applicationId: String,
        newStatus: String
    ): Int
}
