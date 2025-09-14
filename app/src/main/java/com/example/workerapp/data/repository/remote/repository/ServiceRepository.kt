package com.example.workerapp.data.repository.remote.repository

import com.example.workerapp.data.model.cleaning.CleaningServiceModel
import com.example.workerapp.data.model.healthcare.HealthcareServiceModel
import com.example.workerapp.data.repository.remote.NetworkResult

interface ServiceRepository {
    suspend fun getCleaningServices() : NetworkResult<List<CleaningServiceModel>>
    suspend fun getHealthcareServices() : NetworkResult<List<HealthcareServiceModel>>
}