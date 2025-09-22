package com.example.workerapp.data

import com.example.workerapp.data.source.model.cleaning.CleaningServiceModel
import com.example.workerapp.data.source.model.healthcare.HealthcareServiceModel

interface JobServiceRepository {

    suspend fun getCleaningServices(): Result<List<CleaningServiceModel>>

    suspend fun getHealthcareServices(): Result<List<HealthcareServiceModel>>

    suspend fun getHealthcareServiceByUid(uid: String): Result<HealthcareServiceModel>
}
