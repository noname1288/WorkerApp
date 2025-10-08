package com.example.workerapp.data.source.remote

import android.util.Log
import com.example.workerapp.data.source.JobDataSource
import com.example.workerapp.data.source.remote.api.JobApi
import com.example.workerapp.data.source.remote.dto.request.ApplicationRequest
import com.example.workerapp.data.source.model.base.JobModel1
import com.example.workerapp.data.source.model.cleaning.CleaningJobModel1
import com.example.workerapp.data.source.model.healthcare.HealthcareJobModel
import com.example.workerapp.data.source.model.maintenance.MaintenanceJobModel
import com.example.workerapp.data.source.remote.dto.ApplicationWrapper
import com.example.workerapp.data.source.remote.dto.NetworkResult
import com.example.workerapp.data.source.remote.dto.response.ApplicationResponse
import com.example.workerapp.utils.ext.safeApiCall
import javax.inject.Inject

class JobRemoteImpl @Inject constructor(
    private val jobApi: JobApi
) : JobDataSource.Remote {
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

    override suspend fun getMaintenanceJobs(): NetworkResult<List<MaintenanceJobModel>> {
        TODO("Not yet implemented")
    }

    override suspend fun applyForJob(request: ApplicationRequest): NetworkResult<Boolean> {
        val response = safeApiCall(
            apiCall = { jobApi.applyForJob(request) },
            tag = "applyForJob"
        )

        when(response){
            is NetworkResult.Error -> {
                Log.e(TAG, "applyForJob: ${response.message}")
                return NetworkResult.Error(response.message)
            }
            is NetworkResult.Success -> {
                Log.d(TAG, "applyForJob: ${response.data}")
                return NetworkResult.Success(response.data.success)
            }
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

    override suspend fun getApplication(workerId: String): NetworkResult<List<ApplicationWrapper>> {
        try {
            val response = jobApi.getApplicationsByWorkerId(workerId)
            if (response.success) {
                Log.d(TAG, "getApplication: ${response.orders}")
                return NetworkResult.Success(response.orders)
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
