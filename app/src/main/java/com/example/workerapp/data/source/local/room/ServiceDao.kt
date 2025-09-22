package com.example.workerapp.data.source.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.workerapp.data.source.model.cleaning.CleaningServiceModel
import com.example.workerapp.data.source.model.healthcare.HealthcareServiceModel

@Dao
interface ServiceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCleaningService(cleaningService: CleaningServiceModel)

    @Query("SELECT * FROM cleaning_service")
    suspend fun getCleaningServices() : List<CleaningServiceModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHealthcareService(healthcareService: HealthcareServiceModel)

    @Query("SELECT * FROM healthcare_service")
    suspend fun getHealthcareServices() : List<HealthcareServiceModel>

    @Query("SELECT * FROM healthcare_service WHERE uid = :uid LIMIT 1")
    suspend fun getHealthcareServiceByUid(uid: String): HealthcareServiceModel?
}
