package com.example.workerapp.data.source.remote.api

import com.example.workerapp.data.source.remote.dto.BaseResponse
import com.example.workerapp.data.source.remote.dto.response.PolicyResponse
import retrofit2.Response
import retrofit2.http.GET

interface PolicyApi {

    @GET("policies")
    suspend fun getPolicies(): Response<BaseResponse<PolicyResponse>>
}
