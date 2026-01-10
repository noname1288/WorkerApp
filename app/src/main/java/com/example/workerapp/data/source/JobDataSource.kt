package com.example.workerapp.data.source

import com.example.workerapp.data.source.local.room.entity.ApplicationModel
import com.example.workerapp.data.source.model.base.JobModel1
import com.example.workerapp.data.source.model.cleaning.CleaningJobModel1
import com.example.workerapp.data.source.model.healthcare.HealthcareJobModel
import com.example.workerapp.data.source.model.maintenance.MaintenanceJobResponse
import com.example.workerapp.data.source.remote.dto.NetworkResult
import com.example.workerapp.data.source.remote.dto.request.ApplicationRequest
import com.example.workerapp.data.source.remote.dto.request.CancelApplicationRequest
import com.example.workerapp.data.source.remote.dto.response.ApplicationDto
import com.example.workerapp.data.source.remote.dto.response.CancelApplicationWrapper

interface JobDataSource {
    /* *
    * Local
    * */
    interface Local{
        suspend fun getApplicationsFromLocal() : List<ApplicationModel>

        suspend fun saveApplicationsToLocal(applications: List<ApplicationModel>)

        suspend fun upsertApplicationToLocal(applications: List<ApplicationModel>)

        suspend fun addNewApplicationToLocal(application: ApplicationModel)

        suspend fun getApplicationByJobUid(jobUid: String) : Result<List<ApplicationModel>>

        suspend fun updateStatusByApplicationId(applicationId: String, newStatus: String) : Result<Unit>

        suspend fun deleteByApplicationId(applicationId: String)

        suspend fun clearData()
    }


    /* *
    * Remote
    * */
    interface Remote {
        suspend fun getCleaningJobs(): NetworkResult<List<CleaningJobModel1>>
        suspend fun getCleaningDetail(jobUid: String): NetworkResult<CleaningJobModel1>

        suspend fun getHealthcareJobs(): NetworkResult<List<HealthcareJobModel>>
        suspend fun getHealthcareDetail(jobUid: String): NetworkResult<HealthcareJobModel>

        suspend fun getMaintenanceJobs(): NetworkResult<List<MaintenanceJobResponse>>
        suspend fun getMaintenanceDetail(jobUid: String) : NetworkResult<MaintenanceJobResponse>

        suspend fun applyForJob(request: ApplicationRequest): Result<Unit>
        suspend fun cancelApplication(request: CancelApplicationRequest) : Result<CancelApplicationWrapper?>

        suspend fun getSchedules(workerId: String, date: String): NetworkResult<List<JobModel1>>

        suspend fun getApplication(workerId: String) : Result<List<ApplicationDto>>
    }
}

