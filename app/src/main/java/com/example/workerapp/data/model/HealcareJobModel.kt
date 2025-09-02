package com.example.workerapp.data.model

import com.example.workerapp.data.model.base.JobModel

data class HealcareJobModel(
    var id: String,
    val shiftID: String,
    val services: List<String>,

    var jobDetail: JobModel = JobModel()
)

