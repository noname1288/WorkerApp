package com.example.workerapp.data.source.remote.api

import com.example.workerapp.data.source.remote.dto.BaseResponse
import com.example.workerapp.data.source.remote.dto.response.ServiceResponse
import com.example.workerapp.data.source.model.cleaning.CleaningServiceModel
import com.example.workerapp.data.source.model.healthcare.HealthcareServiceModel
import com.example.workerapp.data.source.model.maintenance.MaintenanceServiceModel
import retrofit2.http.GET

interface ServiceApi {
    @GET("services/cleaning")
    suspend fun getCleaningServices() : BaseResponse<ServiceResponse<CleaningServiceModel>>

    @GET("services/healthcare")
    suspend fun getHealthcareServices() : BaseResponse<ServiceResponse<HealthcareServiceModel>>

    @GET("/services/maintenance")
    suspend fun getMaintenanceServices(): BaseResponse<List<MaintenanceServiceModel>>
}