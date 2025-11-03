package com.example.workerapp.data.source.remote.api

import com.example.workerapp.data.source.remote.dto.BaseReviewResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path


interface ReviewApi {

    @GET("reviews/worker/{workerUid}/experience")
    suspend fun getReviews(
        @Path("workerUid") workerUid: String
    ) : Response<BaseReviewResponse>
}
