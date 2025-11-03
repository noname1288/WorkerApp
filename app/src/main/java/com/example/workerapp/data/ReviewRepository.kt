package com.example.workerapp.data

import com.example.workerapp.data.source.remote.dto.NetworkResult
import com.example.workerapp.data.source.remote.dto.response.ReviewResponse

interface ReviewRepository {
    suspend fun getReviews(workerUid: String) : NetworkResult<ReviewResponse>
}