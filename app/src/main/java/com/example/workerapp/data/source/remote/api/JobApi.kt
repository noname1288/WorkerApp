package com.example.workerapp.data.source.remote.api

import com.example.workerapp.data.source.remote.dto.BaseJobResponse
import com.example.workerapp.data.source.remote.dto.BaseJobsResponse
import com.example.workerapp.data.source.remote.dto.request.ApplicationRequest
import com.example.workerapp.data.source.model.base.JobModel1
import com.example.workerapp.data.source.model.cleaning.CleaningJobModel1
import com.example.workerapp.data.source.model.healthcare.HealthcareJobModel
import com.example.workerapp.data.source.remote.dto.BaseApplicationResponse
import com.example.workerapp.data.source.remote.dto.response.ApplicationResponse
import com.example.workerapp.utils.annotation.AuthRequired
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface JobApi {
    @GET("jobs/cleaning")
    suspend fun getCleaningJobs(): BaseJobsResponse<List<CleaningJobModel1>>

    @GET("jobs/cleaning/{uid}")
    suspend fun getCleaningJobByUid(
        @Path("uid") uid: String
    ): BaseJobResponse<CleaningJobModel1>

    @GET("jobs/healthcare")
    suspend fun getHealthcareJobs(): BaseJobsResponse<List<HealthcareJobModel>>

    @GET("jobs/healthcare/{uid}")
    suspend fun getHealthcareJobByUid(
        @Path("uid") uid: String
    ): BaseJobResponse<HealthcareJobModel>

    @AuthRequired
    @POST("orders/create")
    suspend fun applyForJob(
        @Body request: ApplicationRequest
    ): Response<ApplicationResponse>

    @AuthRequired
    @GET("schedules")
    suspend fun getSchedules(
        @Query("date") date: String,
    ): BaseJobsResponse<List<JobModel1>>

    @AuthRequired
    @GET("orders/worker/{workerID}")
    suspend fun getApplicationsByWorkerId(
        @Path("workerID") workerID: String
    ) : BaseApplicationResponse
}
