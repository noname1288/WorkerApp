package com.example.workerapp.data

import com.example.workerapp.data.source.local.room.entity.ApplicationModel

interface JobRepository {
    suspend fun getApplications(): Result<List<ApplicationModel>>

    suspend fun checkApplicationByJobUid(jobUid: String) : Result<Boolean>
    suspend fun cancelJob(serviceType: String, jobUid: String) : Result<Boolean>
    suspend fun insertApplicationToLocal(application: ApplicationModel)
}
