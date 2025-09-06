package com.example.workerapp.data.model

import com.example.workerapp.data.model.base.ServiceModel

data class CleaningServiceModel(
    override val id: String,
    override val duties: List<String> = emptyList(),
    override val imageUrl: String = "",
    override val serviceType: String = ServiceType.HEALTHCARE,
    override val serviceName: String = ""
) : ServiceModel (id, duties, imageUrl, serviceType, serviceName)
