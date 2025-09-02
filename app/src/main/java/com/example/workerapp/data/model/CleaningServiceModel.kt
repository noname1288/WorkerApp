package com.example.workerapp.data.model

import com.example.workerapp.data.model.base.ServiceModel

data class CleaningServiceModel(
    val id: String,
    val image: String,
    val tasks: List<String>,

    var serviceDetail: ServiceModel
)
