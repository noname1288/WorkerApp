package com.example.workerapp.data

import com.example.workerapp.data.source.remote.dto.NetworkResult
import com.example.workerapp.data.source.remote.dto.response.PolicyResponse

interface SystemRepository {
    suspend fun getPolicies(): NetworkResult<PolicyResponse>
}
