package com.example.workerapp.data.source

import com.example.workerapp.data.source.remote.dto.NetworkResult
import com.example.workerapp.data.source.model.cleaning.CleaningServiceModel
import com.example.workerapp.data.source.model.healthcare.HealthcareServiceModel
import com.example.workerapp.data.source.model.maintenance.MaintenanceServiceModel
import com.example.workerapp.data.source.model.maintenance.PowerModel
import com.example.workerapp.data.source.remote.dto.response.MaintenanceServiceResponse

interface JobServiceDataSource {
    /* *
    * Local
    * */
    interface Local {
        /* *
        * CLEANING SERVICE
        * */
        suspend fun getCleaningServices(): List<CleaningServiceModel>

        suspend fun saveCleaningServices(services: List<CleaningServiceModel>)

        /* *
        * HEALTHCARE SERVICE
        * */
        suspend fun getHealthcareServices(): List<HealthcareServiceModel>

        suspend fun getHealthcareServiceByUid(uid: String): HealthcareServiceModel?

        suspend fun saveHealthcareServices(services: List<HealthcareServiceModel>)

        /* *
        * MAINTENANCE SERVICE
        * */
        suspend fun getAllMaintenanceServices(): List<MaintenanceServiceModel>

        suspend fun getAllPowers(): List<PowerModel>

        suspend fun saveMaintenanceService(service: MaintenanceServiceModel)

        suspend fun savePowers(powers: List<PowerModel>)

    }

    /* *
    * Remote
    * */
    interface Remote {

        suspend fun getCleaningServices(): NetworkResult<List<CleaningServiceModel>>

        suspend fun getHealthcareServices(): NetworkResult<List<HealthcareServiceModel>>

        suspend fun getMaintenanceServices(): NetworkResult<List<MaintenanceServiceResponse>>
    }
}