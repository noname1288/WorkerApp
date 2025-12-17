package com.example.workerapp.data.source.remote.dto.response

import com.example.workerapp.data.source.local.room.entity.ApplicationModel
import com.example.workerapp.data.source.model.base.JobModel1
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApplicationDto(
    val uid: String,
    val job: JobModel1,
    val isReview: Boolean,
    val status: String,
    val createdAt: String,
    val serviceType: String,
)

fun ApplicationDto.toEntity() : ApplicationModel{
    return ApplicationModel(
        id = 0L, // Room auto-generate
        applicationId = uid,
        jobId = job.uid, // lấy từ JobModel1
        createdAt = createdAt,
        serviceType = serviceType
    )
}
