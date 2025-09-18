package com.example.workerapp.data.source.model

data class OrderModel(
    val uid: String,
    val workerID: String,
    val jobID: String,
    val serviceType: String,
    val status: String
)
