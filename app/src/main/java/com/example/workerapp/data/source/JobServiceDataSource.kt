package com.example.workerapp.data.source

import com.example.workerapp.data.source.remote.dto.NetworkResult
import com.example.workerapp.data.source.model.cleaning.CleaningServiceModel
import com.example.workerapp.data.source.model.healthcare.HealthcareServiceModel

interface JobServiceDataSource {
    /* *
    * Local
    * */
    interface Local {
        suspend fun getCleaningServices(): List<CleaningServiceModel>
        suspend fun getHealthcareServices(): List<HealthcareServiceModel>
        suspend fun saveCleaningServices(services: List<CleaningServiceModel>)
        suspend fun saveHealthcareServices(services: List<HealthcareServiceModel>)
    }

    /* *
    * Remote
    * */
    interface Remote {
        suspend fun getCleaningServices(): NetworkResult<List<CleaningServiceModel>>
        suspend fun getHealthcareServices(): NetworkResult<List<HealthcareServiceModel>>
    }
}