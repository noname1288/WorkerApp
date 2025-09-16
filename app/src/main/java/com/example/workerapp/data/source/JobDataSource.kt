package com.example.workerapp.data.source

import com.example.workerapp.data.source.remote.NetworkResult
import com.example.workerapp.data.source.remote.dto.request.ApplicationRequest
import com.example.workerapp.data.source.remote.model.base.JobModel1
import com.example.workerapp.data.source.remote.model.cleaning.CleaningJobModel1
import com.example.workerapp.data.source.remote.model.healthcare.HealthcareJobModel

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

        suspend fun applyForJob(request: ApplicationRequest): NetworkResult<Boolean>

        suspend fun getSchedules(workerId: String, date: String): NetworkResult<List<JobModel1>>
    }
}

