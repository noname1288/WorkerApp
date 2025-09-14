package com.example.workerapp.data.repository.remote.api

import com.example.workerapp.data.model.cleaning.CleaningServiceModel
import com.example.workerapp.data.model.healthcare.HealthcareServiceModel
import com.example.workerapp.data.repository.remote.dto.BaseResponse
import com.example.workerapp.data.repository.remote.dto.response.ServiceResponse
import retrofit2.http.GET

interface ServiceApi {
    @GET("services/cleaning")
    suspend fun getCleaningServices() : BaseResponse<ServiceResponse<CleaningServiceModel>>

    @GET("services/healthcare")
    suspend fun getHealthcareServices() : BaseResponse<ServiceResponse<HealthcareServiceModel>>
}