package com.example.workerapp.data.source.remote

import android.util.Log
import com.example.workerapp.data.error.AppError
import com.example.workerapp.data.source.JobDataSource
import com.example.workerapp.data.source.model.base.JobModel1
import com.example.workerapp.data.source.model.cleaning.CleaningJobModel1
import com.example.workerapp.data.source.model.healthcare.HealthcareJobModel
import com.example.workerapp.data.source.model.maintenance.MaintenanceJobResponse
import com.example.workerapp.data.source.remote.api.JobApi
import com.example.workerapp.data.source.remote.dto.NetworkResult
import com.example.workerapp.data.source.remote.dto.request.ApplicationRequest
import com.example.workerapp.data.source.remote.dto.request.CancelApplicationRequest
import com.example.workerapp.data.source.remote.dto.response.ApiErrorResponse
import com.example.workerapp.data.source.remote.dto.response.ApplicationDto
import com.example.workerapp.data.source.remote.dto.response.CancelApplicationWrapper
import com.squareup.moshi.Moshi
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class JobRemoteImpl @Inject constructor(
    private val jobApi: JobApi,
    moshi: Moshi
) : JobDataSource.Remote {

    private val errorAdapter = moshi.adapter(ApiErrorResponse::class.java)

    private val APPLICATION_CREATED_AT_FORMATTER =
        DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy")

    private val JOB_CREATED_AT_FORMATTER =
        DateTimeFormatter.ofPattern("dd/MM/yyyy")

    override suspend fun getCleaningJobs(): NetworkResult<List<CleaningJobModel1>> {
        try {
            val result = jobApi.getCleaningJobs()
            if (result.success) {
                val jobs = result.jobs ?: emptyList()
                val sortedJobs = jobs.sortedByDescending { job ->
                    try {
                        if (job.createdAt.isNotEmpty()) {
                            LocalDate.parse(job.createdAt, JOB_CREATED_AT_FORMATTER)
                        } else {
                            LocalDate.MIN
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "getCleaningJobs: Failed to parse createdAt for job ${job.uid}: ${e.message}")
                        LocalDate.MIN
                    }
                }
                Log.d(TAG, "jobs: ${sortedJobs.size} jobs sorted by createdAt")
                return NetworkResult.Success(sortedJobs)
            } else {
                Log.e(TAG, "getCleaningJobs: ${result.message}")
                return NetworkResult.Error(result.message)
            }
        } catch (e: Exception) {
            Log.e(TAG, "getCleaningJobs: ${e.message}")
            return NetworkResult.Error(e.message ?: "Unknown error")
        }
    }

    override suspend fun getCleaningDetail(jobUid: String): NetworkResult<CleaningJobModel1> {
        try {
            val result = jobApi.getCleaningJobByUid(jobUid)
            if (result.success) {
                Log.d(TAG, "job detail: ${result.job}")
                return NetworkResult.Success(result.job ?: CleaningJobModel1())
            } else {
                Log.e(TAG, "getCleaningDetail: ${result.message}")
                return NetworkResult.Error(result.message)
            }
        } catch (e: Exception) {
            Log.e(TAG, "getCleaningDetail: ${e.message}")
            return NetworkResult.Error(e.message ?: "Unknown error")
        }
    }

    override suspend fun getHealthcareJobs(): NetworkResult<List<HealthcareJobModel>> {
        try {
            val result = jobApi.getHealthcareJobs()
            if (result.success) {
                val jobs = result.jobs ?: emptyList()
                val sortedJobs = jobs.sortedByDescending { job ->
                    try {
                        if (job.createdAt.isNotEmpty()) {
                            LocalDate.parse(job.createdAt, JOB_CREATED_AT_FORMATTER)
                        } else {
                            LocalDate.MIN
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "getHealthcareJobs: Failed to parse createdAt for job ${job.uid}: ${e.message}")
                        LocalDate.MIN
                    }
                }
                Log.d(TAG, "jobs: ${sortedJobs.size} jobs sorted by createdAt")
                return NetworkResult.Success(sortedJobs)
            } else {
                Log.e(TAG, "getHealthcareJobs: ${result.message}")
                return NetworkResult.Error(result.message)
            }
        } catch (e: Exception) {
            Log.e(TAG, "getHealthcareJobs: ${e.message}")
            return NetworkResult.Error(e.message ?: "Unknown error")
        }
    }

    override suspend fun getHealthcareDetail(jobUid: String): NetworkResult<HealthcareJobModel> {
        try {
            val result = jobApi.getHealthcareJobByUid(jobUid)
            if (result.success) {
                Log.d(TAG, "job detail: ${result.job}")
                return NetworkResult.Success(result.job ?: HealthcareJobModel())
            } else {
                Log.e(TAG, "getHealthcareDetail: ${result.message}")
                return NetworkResult.Error(result.message)
            }
        } catch (e: Exception) {
            Log.e(TAG, "getHealthcareDetail: ${e.message}")
            return NetworkResult.Error(e.message ?: "Unknown error")
        }
    }

    override suspend fun getMaintenanceJobs(): NetworkResult<List<MaintenanceJobResponse>> {
        return try {
            val response = jobApi.getMaintenanceJobs()
            if (response.success) {
                val jobs = response.jobs ?: emptyList()
                val sortedJobs = jobs.sortedByDescending { job ->
                    try {
                        if (job.createdAt.isNotEmpty()) {
                            LocalDate.parse(job.createdAt, JOB_CREATED_AT_FORMATTER)
                        } else {
                            LocalDate.MIN
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "getMaintenanceJobs: Failed to parse createdAt for job ${job.uid}: ${e.message}")
                        LocalDate.MIN
                    }
                }
                Log.d(TAG, "getMaintenanceJobs: ${sortedJobs.size} jobs sorted by createdAt")
                Log.d(TAG, "getMaintenanceJobs: $sortedJobs ")
                NetworkResult.Success(sortedJobs)
            } else {
                Log.e(TAG, "getMaintenanceJobs Error: ${response.message}")
                NetworkResult.Error(response.message)
            }
        } catch (e: Exception) {
            Log.e(TAG, "getMaintenanceJobs Exception: ${e.message}")
            NetworkResult.Error(e.message ?: "Unknown error")
        }
    }

    override suspend fun getMaintenanceDetail(jobUid: String): NetworkResult<MaintenanceJobResponse> {
        return try {
            val response = jobApi.getMaintenanceJobByUid(jobUid)

            if (response.success) {
                Log.d(TAG, "getMaintenanceDetail: ${response.job}")
                NetworkResult.Success(response.job ?: MaintenanceJobResponse())
            } else {
                Log.e(TAG, "getMaintenanceDetail Error: ${response.message}")
                NetworkResult.Error(response.message)
            }
        } catch (e: Exception) {
            Log.e(TAG, "getMaintenanceDetail Exception: ${e.message}")
            NetworkResult.Error(e.message ?: "Unknown error")
        }
    }

    override suspend fun applyForJob(request: ApplicationRequest): Result<Unit> {
        return runCatching {
            val response = jobApi.applyForJob(request)

            if (!response.isSuccessful){
                val errorMessage = response.errorBody()?.string()
                    ?.let { json -> errorAdapter.fromJson(json)?.error }
                    ?: response.message()
                    ?: "Request failed with status code ${response.code()}"

                throw AppError.Network(
                    errorMessage = errorMessage,
                    httpCode = response.code()
                )
            }

            val body = response.body() ?: throw AppError.Network("Empty Body")

            if (!body.success){
                throw AppError.Business(body.message ?: "Apply failed")
            }

            Unit
        }
    }

    override suspend fun cancelApplication(
        request: CancelApplicationRequest
    ): Result<CancelApplicationWrapper?> {
        return runCatching {
            val response = jobApi.cancelApplication(request)

            if (!response.isSuccessful){
                val errorMessage = response.errorBody()?.string()
                ?.let { json -> errorAdapter.fromJson(json)?.error }
                ?: response.message()
                ?: "Request failed with status code ${response.code()}"

                throw AppError.Network(
                    errorMessage,
                    response.code()
                )
            }

            val body = response.body() ?: throw AppError.Network("Empty Body")

            if (!body.success){
                throw AppError.Business(body.message ?: "Cancel job failed")
            }

            body.updatedOrder
        }
    }

    override suspend fun getSchedules(
        workerId: String,
        date: String
    ): NetworkResult<List<JobModel1>> {
        return try {
            val response = jobApi.getSchedules(date)
            if (response.success) {
                Log.d(TAG, "getSchedules: ${response.jobs}")
                NetworkResult.Success(response.jobs ?: emptyList())
            } else {
                Log.e(TAG, "getSchedules Error: ${response.message}")
                NetworkResult.Error(response.message)
            }
        } catch (e: Exception) {
            Log.e(TAG, "getSchedules Exception: ${e.message}")
            NetworkResult.Error(e.message ?: "Unknown error")
        }
    }

    override suspend fun getApplication(workerId: String): Result<List<ApplicationDto>> {
        return runCatching {
            val response = jobApi.getApplicationsByWorkerId(workerId)

            if (!response.success)
                throw AppError.Network(response.message ?: "Error to get all applications")

            Log.d(TAG, "getApplication: ${response.orders}")
            response.orders.sortedByDescending {
                LocalDateTime.parse(it.createdAt, APPLICATION_CREATED_AT_FORMATTER)
            }
        }
    }

    companion object {
        private const val TAG = "JobRemoteImpl"
    }
}
