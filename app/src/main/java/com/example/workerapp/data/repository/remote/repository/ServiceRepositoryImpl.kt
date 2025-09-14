package com.example.workerapp.data.repository.remote.repository

import android.util.Log
import com.example.workerapp.data.model.cleaning.CleaningServiceModel
import com.example.workerapp.data.model.healthcare.HealthcareServiceModel
import com.example.workerapp.data.repository.remote.NetworkResult
import com.example.workerapp.data.repository.remote.RetrofitHelper
import com.example.workerapp.data.repository.remote.api.ServiceApi

class ServiceRepositoryImpl(private val serviceApi: ServiceApi) : ServiceRepository {
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
        val TAG  = "ServiceRepositoryImpl"

        var singleton : ServiceRepositoryImpl? = null

        fun getInstance() : ServiceRepositoryImpl{
            if (singleton == null){
                singleton = ServiceRepositoryImpl(RetrofitHelper.serviceApi)
            }
            return singleton!!
        }
    }
}