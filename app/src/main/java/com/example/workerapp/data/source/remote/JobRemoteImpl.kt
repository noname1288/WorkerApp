package com.example.workerapp.data.source.remote

import android.util.Log
import com.example.workerapp.data.source.JobDataSource
import com.example.workerapp.data.source.model.base.JobModel1
import com.example.workerapp.data.source.model.cleaning.CleaningJobModel1
import com.example.workerapp.data.source.model.healthcare.HealthcareJobModel
import com.example.workerapp.data.source.model.maintenance.MaintenanceJobResponse
import com.example.workerapp.data.source.remote.api.JobApi
import com.example.workerapp.data.source.remote.dto.NetworkResult
import com.example.workerapp.data.source.remote.dto.request.ApplicationRequest
import com.example.workerapp.data.source.remote.dto.response.ApiErrorResponse
import com.example.workerapp.data.source.remote.dto.response.ApplicationDto
import com.squareup.moshi.Moshi
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class JobRemoteImpl @Inject constructor(
    private val jobApi: JobApi,
    moshi: Moshi
) : JobDataSource.Remote {

    private val errorAdapter = moshi.adapter(ApiErrorResponse::class.java)

    private val CREATED_AT_FORMATTER =
        DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy")

    override suspend fun getCleaningJobs(): NetworkResult<List<CleaningJobModel1>> {
        try {
            val result = jobApi.getCleaningJobs()
            if (result.success) {
                Log.d(TAG, "jobs: ${result.jobs}")
                return NetworkResult.Success(result.jobs ?: emptyList())
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
                Log.d(TAG, "jobs: ${result.jobs}")
                return NetworkResult.Success(result.jobs ?: emptyList())
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
                Log.d(TAG, "getMaintenanceJobs: ${response.jobs}")
                NetworkResult.Success(response.jobs ?: emptyList())
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

    override suspend fun applyForJob(request: ApplicationRequest): NetworkResult<Boolean> {
        val response = jobApi.applyForJob(request)

        return if (response.isSuccessful) {
            val body = response.body()

            if (body != null && body.success) {
                Log.d(TAG, "applyForJob: ${body.message}")
                NetworkResult.Success(true)
            } else {
                Log.e(TAG, "applyForJob Error: ${body?.message ?: "Empty response body"}")
                NetworkResult.Error(body?.message ?: "Empty response body")
            }
        } else {
            val errorMessage = response.errorBody()?.string()
                ?.let { json -> errorAdapter.fromJson(json)?.error }
                ?: response.message()
                ?: "Request failed with status code ${response.code()}"

            NetworkResult.Error(errorMessage)
        }
    }

    override suspend fun cancelJob(
        serviceType: String,
        jobUid: String
    ): NetworkResult<Boolean> {
        val response = jobApi.cancelJob(serviceType, jobUid)

        return if (response.isSuccessful){
            val body = response.body()

            if (body != null && body.success){
                Log.d(TAG, "cancelJob: ${body.message}")
                return NetworkResult.Success(true)
            } else {
                Log.e(TAG, "cancelJob Error: ${body?.message ?: "Empty response body"}")
                return NetworkResult.Error(body?.message ?: "Empty response body")
            }
        } else {
            val errorMessage = response.errorBody()?.string()
                ?.let { json -> errorAdapter.fromJson(json)?.error }
                ?: response.message()
                ?: "Request failed with status code ${response.code()}"

            NetworkResult.Error(errorMessage)
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

    override suspend fun getApplication(workerId: String): NetworkResult<List<ApplicationDto>> {
        try {
            val response = jobApi.getApplicationsByWorkerId(workerId)
            if (response.success) {
                val sortedOrders = response.orders.sortedByDescending {
                    LocalDateTime.parse(it.createdAt, CREATED_AT_FORMATTER)
                }

                Log.d(TAG, "getApplication: ${response.orders}")
                return NetworkResult.Success(sortedOrders)
            } else {
                Log.e(TAG, "getApplication Error: ${response.message}")
                return NetworkResult.Error(response.message)
            }
        } catch (e: Exception) {
            Log.e(TAG, "getApplication Exception: ${e.message}")
            return NetworkResult.Error(e.message ?: "Unknown error")
        }
    }

    companion object {
        private const val TAG = "JobRemoteImpl"
    }
}
