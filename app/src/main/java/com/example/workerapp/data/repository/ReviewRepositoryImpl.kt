package com.example.workerapp.data.repository

import com.example.workerapp.data.ReviewRepository
import com.example.workerapp.data.source.remote.api.ReviewApi
import com.example.workerapp.data.source.remote.dto.NetworkResult
import com.example.workerapp.data.source.remote.dto.response.ApiErrorResponse
import com.example.workerapp.data.source.remote.dto.response.ReviewResponse
import com.squareup.moshi.Moshi
import javax.inject.Inject

class ReviewRepositoryImpl @Inject constructor(
    private val reviewApi: ReviewApi,
    moshi: Moshi
) : ReviewRepository {

    private val errorAdapter = moshi.adapter(ApiErrorResponse::class.java)

    override suspend fun getReviews(workerUid: String): NetworkResult<ReviewResponse> {
        return try {
            val response = reviewApi.getReviews(workerUid)

            if (response.isSuccessful) {
                val body = response.body()

                if (body != null && body.success && body.experiences != null) {
                    NetworkResult.Success(body.experiences)
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
            // catch tất cả exception từ network / parse / IO
            NetworkResult.Error(e.localizedMessage ?: "Unexpected error occurred")
        }
    }
}
