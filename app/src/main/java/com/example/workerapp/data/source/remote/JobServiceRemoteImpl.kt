package com.example.workerapp.data.source.remote

import android.util.Log
import com.example.workerapp.data.source.JobServiceDataSource
import com.example.workerapp.data.source.remote.api.ServiceApi
import com.example.workerapp.data.source.model.cleaning.CleaningServiceModel
import com.example.workerapp.data.source.model.healthcare.HealthcareServiceModel
import com.example.workerapp.data.source.model.maintenance.MaintenanceServiceModel
import com.example.workerapp.data.source.remote.dto.NetworkResult
import javax.inject.Inject

class JobServiceRemoteImpl @Inject constructor(
    private val serviceApi: ServiceApi
) : JobServiceDataSource.Remote {
    override suspend fun getCleaningServices(): NetworkResult<List<CleaningServiceModel>> {
        return try {
            val response = serviceApi.getCleaningServices()
            if (response.success) {
                Log.d(TAG, "getCleaningServices: ${response.data.services}")
                NetworkResult.Success(response.data.services)
            } else {
                Log.e(TAG, "getCleaningServices Error: ${response.message}")
                NetworkResult.Error(response.message)
            }
        } catch (e: Exception) {
            Log.e(TAG, "getCleaningServices Exception: ${e.message}")
            NetworkResult.Error(e.message ?: "Unknown error")
        }
    }

    override suspend fun getHealthcareServices(): NetworkResult<List<HealthcareServiceModel>> {
        return try {
            val response = serviceApi.getHealthcareServices()
            if (response.success) {
                Log.d(TAG, "getHealthcareServices: ${response.data.services}")
                NetworkResult.Success(response.data.services)
            } else {
                Log.e(TAG, "getHealthcareServices Error: ${response.message}")
                NetworkResult.Error(response.message)
            }
        } catch (e: Exception) {
            Log.e(TAG, "getHealthcareServices Exception: ${e.message}")
            NetworkResult.Error(e.message ?: "Unknown error")
        }
    }

    override suspend fun getMaintenanceServices(): NetworkResult<List<MaintenanceServiceModel>> {
        return try {
            val response = serviceApi.getMaintenanceServices()

            if (response.success) {
                Log.d(TAG, "getMaintenanceServices: ${response.data}")
                NetworkResult.Success(response.data)
            } else {
                Log.e(TAG, "getMaintenanceServices Error: ${response.message}")
                NetworkResult.Error(response.message)
            }
        } catch (e: Exception) {
            Log.e(TAG, "getMaintenanceServices Exception: ${e.message}")
            NetworkResult.Error(e.message ?: "Unknown error")
        }
    }

    companion object {
        val TAG = "JobServiceRemoteImpl"
    }
}