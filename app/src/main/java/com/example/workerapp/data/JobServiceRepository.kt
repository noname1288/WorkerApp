package com.example.workerapp.data

import com.example.workerapp.data.source.model.cleaning.CleaningServiceModel
import com.example.workerapp.data.source.model.healthcare.HealthcareServiceModel
import com.example.workerapp.data.source.model.maintenance.MaintenanceServiceModel
import com.example.workerapp.data.source.model.maintenance.PowerModel

interface JobServiceRepository {

    suspend fun getCleaningServices(): Result<List<CleaningServiceModel>>

    suspend fun getHealthcareServices(): Result<List<HealthcareServiceModel>>

    suspend fun getHealthcareServiceByUid(uid: String): Result<HealthcareServiceModel>

    suspend fun getMaintenanceServices() : Result<List<MaintenanceServiceModel>>

    suspend fun getMaintenanceServiceByUid(uid: String) : Result<MaintenanceServiceModel>
    suspend fun getPowerModelByUid(uid: String) : Result<PowerModel>

    suspend fun getPowers() : Result<List<PowerModel>>
}
