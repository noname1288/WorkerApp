package com.example.workerapp.data.repository

import com.example.workerapp.data.SystemRepository
import com.example.workerapp.data.source.remote.api.PolicyApi
import com.example.workerapp.data.source.remote.dto.NetworkResult
import com.example.workerapp.data.source.remote.dto.response.ApiErrorResponse
import com.example.workerapp.data.source.remote.dto.response.PolicyResponse
import com.squareup.moshi.Moshi
import javax.inject.Inject

class SystemRepositoryImpl @Inject constructor(
    private val policyApi: PolicyApi,
    moshi: Moshi
) : SystemRepository {
    val errorAdapter = moshi.adapter(ApiErrorResponse :: class.java)

    override suspend fun getPolicies(): NetworkResult<PolicyResponse> {
        return try {
            val response = policyApi.getPolicies()

            if (response.isSuccessful) {
                val body = response.body()

                if (body != null && body.success && body.data != null) {
                    NetworkResult.Success(body.data)
                } else {
                    NetworkResult.Error(body?.message ?: "Empty response body or data")
                }
            } else {
                val errorMessage = response.errorBody()?.string()
                    ?.let { json -> errorAdapter.fromJson(json)?.error }
                    ?: response.message()
                    ?: "Request failed with status code ${response.code()}"

                NetworkResult.Error(errorMessage)
            }
        } catch (e: Exception) {
            // ✅ catch tất cả các lỗi network hoặc parse
            NetworkResult.Error(e.localizedMessage ?: "Unexpected error occurred")
        }
    }
}
