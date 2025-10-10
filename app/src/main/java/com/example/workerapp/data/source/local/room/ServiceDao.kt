package com.example.workerapp.data.source.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.workerapp.data.source.model.cleaning.CleaningServiceModel
import com.example.workerapp.data.source.model.healthcare.HealthcareServiceModel
import com.example.workerapp.data.source.model.maintenance.MaintenanceServiceModel
import com.example.workerapp.data.source.model.maintenance.PowerModel

@Dao
interface ServiceDao {
    /* *
    * CLEANING
    * */

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCleaningService(cleaningService: List<CleaningServiceModel>)

    @Query("SELECT * FROM cleaning_service")
    suspend fun getCleaningServices(): List<CleaningServiceModel>

    /* *
    * HEALTHCARE
    * */

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHealthcareService(healthcareServices: List<HealthcareServiceModel>)

    @Query("SELECT * FROM healthcare_service")
    suspend fun getHealthcareServices(): List<HealthcareServiceModel>

    @Query("SELECT * FROM healthcare_service WHERE uid = :uid LIMIT 1")
    suspend fun getHealthcareServiceByUid(uid: String): HealthcareServiceModel?

    /* *
    * MAINTENANCE
    * */

    @Query("SELECT * FROM maintenance_service")
    suspend fun getAllMaintenance(): List<MaintenanceServiceModel>

    @Query("SELECT * FROM power_service")
    suspend fun getAllPowers(): List<PowerModel>

    @Query("SELECT * FROM maintenance_service WHERE uid = :uid LIMIT 1")
    suspend fun getMaintenanceByUid(uid: String): MaintenanceServiceModel?

    @Query("SELECT * FROM power_service WHERE uid = :uid LIMIT 1")
    suspend fun getPowerByUid(uid: String): PowerModel?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMaintenance(service: MaintenanceServiceModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPowers(powerModels: List<PowerModel>)

    @Transaction
    suspend fun insertMaintenanceServiceWithPowers(
        maintenance: MaintenanceServiceModel,
        powers: List<PowerModel>
    ) {
        insertMaintenance(maintenance)
        insertPowers(powers)
    }
}
