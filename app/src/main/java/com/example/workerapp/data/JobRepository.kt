package com.example.workerapp.data

import com.example.workerapp.data.source.local.room.entity.ApplicationModel
import com.example.workerapp.data.source.remote.dto.response.ApplicationDto

interface JobRepository {
    suspend fun getApplications(): Result<List<ApplicationModel>>

    suspend fun checkApplicationByJobUid(jobUid: String) : Result<String?>
    suspend fun cancelJob(serviceType: String, jobUid: String) : Result<Boolean>
    suspend fun insertApplicationToLocal(application: ApplicationDto) : Result<Boolean>
    suspend fun deleteCurrentApplication(applicationId: String) : Result<Boolean>
}
