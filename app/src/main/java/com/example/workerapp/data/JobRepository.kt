package com.example.workerapp.data

import com.example.workerapp.data.source.local.room.entity.ApplicationModel
import com.example.workerapp.data.source.remote.dto.request.CancelApplicationRequest
import com.example.workerapp.data.source.remote.dto.response.ApplicationDto

interface JobRepository {
    suspend fun getApplications(): Result<List<ApplicationModel>>
    suspend fun getApplicationByJobId(jobUid: String) : Result<ApplicationModel?>
    suspend fun cancelJob(request: CancelApplicationRequest) : Result<String>
    suspend fun insertApplicationToLocal(application: ApplicationDto) : Result<Boolean>
    suspend fun deleteCurrentApplication(applicationId: String) : Result<Boolean>
}
