package com.example.workerapp.data.source

import com.example.workerapp.data.source.model.base.JobModel1
import com.example.workerapp.data.source.model.cleaning.CleaningJobModel1
import com.example.workerapp.data.source.model.healthcare.HealthcareJobModel
import com.example.workerapp.data.source.model.maintenance.MaintenanceJobResponse
import com.example.workerapp.data.source.remote.dto.ApplicationDto
import com.example.workerapp.data.source.remote.dto.NetworkResult
import com.example.workerapp.data.source.remote.dto.request.ApplicationRequest

interface JobDataSource {
    /* *
    * Local
    * */


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

        suspend fun applyForJob(request: ApplicationRequest): NetworkResult<Boolean>

        suspend fun getSchedules(workerId: String, date: String): NetworkResult<List<JobModel1>>

        suspend fun getApplication(workerId: String) : NetworkResult<List<ApplicationDto>>
    }
}

