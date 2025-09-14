package com.example.workerapp.data.repository.remote.api

import com.example.workerapp.data.model.cleaning.CleaningJobModel1
import com.example.workerapp.data.model.healthcare.HealthcareJobModel
import com.example.workerapp.data.repository.remote.dto.BaseJobsResponse
import com.example.workerapp.data.repository.remote.dto.BaseJobResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface JobApi {
    @GET("jobs/cleaning")
    suspend fun getCleaningJobs(): BaseJobsResponse<List<CleaningJobModel1>>

    @GET("jobs/cleaning/{uid}")
    suspend fun getCleaingJobByUid(
        @Path("uid") uid: String
    ): BaseJobResponse<CleaningJobModel1>

    @GET("jobs/healthcare")
    suspend fun getHealthcareJobs(): BaseJobsResponse<List<HealthcareJobModel>>

    @GET("jobs/healthcare/{uid}")
    suspend fun getHealthcareJobByUid(
        @Path("uid") uid: String
    ): BaseJobResponse<HealthcareJobModel>

}
