package com.example.workerapp.data

import com.example.workerapp.data.source.local.room.entity.ApplicationModel
import com.example.workerapp.data.source.remote.dto.request.ApplicationRequest
import com.example.workerapp.data.source.remote.dto.request.CancelApplicationRequest
import com.example.workerapp.data.source.remote.dto.response.ApplicationDto
import com.example.workerapp.data.source.remote.dto.response.CancelApplicationWrapper

interface JobRepository {
    suspend fun getApplications(): Result<List<ApplicationModel>>
    suspend fun getApplicationByJobId(jobUid: String) : Result<ApplicationModel?>
    suspend fun cancelJob(request: CancelApplicationRequest) : Result<CancelApplicationWrapper>
    suspend fun insertApplicationToLocal(application: ApplicationDto) : Result<Boolean>
    suspend fun updateStatusByApplicationId(applicationId: String, newStatus: String) : Result<Unit>
    suspend fun deleteCurrentApplication(applicationId: String) : Result<Boolean>
    suspend fun applyJob(request: ApplicationRequest) : Result<Unit>
}
