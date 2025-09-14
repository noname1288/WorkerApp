package com.example.workerapp.data.repository.remote.repository

import android.util.Log
import com.example.workerapp.data.model.cleaning.CleaningJobModel1
import com.example.workerapp.data.model.healthcare.HealthcareJobModel
import com.example.workerapp.data.repository.remote.NetworkResult
import com.example.workerapp.data.repository.remote.RetrofitHelper
import com.example.workerapp.data.repository.remote.api.JobApi

class JobRepositoryImpl(private val jobApi: JobApi) : JobRepository {
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

    override suspend fun getCleaningDetail(uid: String): NetworkResult<CleaningJobModel1> {
        try {
            val result = jobApi.getCleaingJobByUid(uid)
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

    override suspend fun getHealthcareDetail(uid: String): NetworkResult<HealthcareJobModel> {
        try {
            val result = jobApi.getHealthcareJobByUid(uid)
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

    companion object {
        private const val TAG = "JobRepositoryImpl"

        private var singleton: JobRepositoryImpl? = null

        fun getInstance(): JobRepositoryImpl {
            if (singleton == null) {
                singleton = JobRepositoryImpl(RetrofitHelper.jobApi)
            }
            return singleton!!
        }
    }
}
