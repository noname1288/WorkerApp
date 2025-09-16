package com.example.workerapp.data.source.remote

import android.util.Log
import com.example.workerapp.data.source.JobServiceDataSource
import com.example.workerapp.data.source.remote.api.ServiceApi
import com.example.workerapp.data.source.remote.model.cleaning.CleaningServiceModel
import com.example.workerapp.data.source.remote.model.healthcare.HealthcareServiceModel

class JobServiceRemoteImpl(private val serviceApi: ServiceApi) : JobServiceDataSource.Remote {
    override suspend fun getCleaningServices(): NetworkResult<List<CleaningServiceModel>> {
        return try {
            val response = serviceApi.getCleaningServices()
            if (response.success){
                Log.d(TAG, "getCleaningServices: ${response.data.services}")
                NetworkResult.Success(response.data.services )
            } else {
                Log.e(TAG, "getCleaningServices Error: ${response.message}")
                NetworkResult.Error(response.message)
            }
        }catch (e: Exception){
            Log.e(TAG, "getCleaningServices Exception: ${e.message}")
            NetworkResult.Error(e.message ?: "Unknown error")
        }
    }

    override suspend fun getHealthcareServices(): NetworkResult<List<HealthcareServiceModel>> {
        return try {
            val response = serviceApi.getHealthcareServices()
            if (response.success){
                Log.d(TAG, "getHealthcareServices: ${response.data.services}")
                NetworkResult.Success(response.data.services )
            } else {
                Log.e(TAG, "getHealthcareServices Error: ${response.message}")
                NetworkResult.Error(response.message)
            }
        }catch (e: Exception){
            Log.e(TAG, "getHealthcareServices Exception: ${e.message}")
            NetworkResult.Error(e.message ?: "Unknown error")
        }
    }

    companion object{
        val TAG  = "JobServiceRemoteImpl"

        var singleton : JobServiceRemoteImpl? = null

        fun getInstance() : JobServiceRemoteImpl{
            if (singleton == null){
                singleton = JobServiceRemoteImpl(RetrofitHelper.serviceApi)
            }
            return singleton!!
        }
    }
}