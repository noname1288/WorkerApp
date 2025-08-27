package com.example.workerapp.data.model

import com.example.workerapp.data.model.base.ServiceModel

data class HealthcareServiceModel(
    val uid: String,
    val duties: List<String>,
    val excludedTasks: List<String>,

    var serviceDetail: ServiceModel
)
