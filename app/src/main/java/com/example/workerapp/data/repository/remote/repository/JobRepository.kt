package com.example.workerapp.data.repository.remote.repository

import com.example.workerapp.data.model.cleaning.CleaningJobModel1
import com.example.workerapp.data.model.healthcare.HealthcareJobModel
import com.example.workerapp.data.repository.remote.NetworkResult

interface JobRepository {
    suspend fun getCleaningJobs(): NetworkResult<List<CleaningJobModel1>>
    suspend fun getCleaningDetail(uid: String): NetworkResult<CleaningJobModel1>

    suspend fun getHealthcareJobs(): NetworkResult<List<HealthcareJobModel>>
    suspend fun getHealthcareDetail(uid: String): NetworkResult<HealthcareJobModel>
}
