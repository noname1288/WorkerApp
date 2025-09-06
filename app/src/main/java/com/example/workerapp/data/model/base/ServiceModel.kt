package com.example.workerapp.data.model.base

open class ServiceModel(
    open val id: String,
    open val duties: List<String>,
    open val imageUrl: String,
    open val serviceType: String,
    open val serviceName: String
){
    companion object ServiceType{
        const val CLEANING = "CLEANING"
        const val HEALTHCARE = "HEALTHCARE"
    }
}