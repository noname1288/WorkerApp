package com.example.workerapp.data.model

import com.example.workerapp.data.model.base.JobModel

data class CleaningJobModel(
    var id: String = "",
    var durationID: String = "",
    val services: List<String> = emptyList(),
    val isCooking: Boolean = false,
    val isIroning: Boolean = false,

    val jobDetail: JobModel = JobModel()
)