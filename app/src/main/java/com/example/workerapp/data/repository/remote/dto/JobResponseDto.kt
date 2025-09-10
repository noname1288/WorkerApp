package com.example.workerapp.data.repository.remote.dto

data class JobResponseDto(
    val id: String,
    val clientName: String,
    val phoneNumber: String,
    val address: String,
    val workerQuantity: Int,
    val serviceType: String,
    val startTime: Long,
    val endTime: Long,
    val price: Double,
    val isWeekly: Boolean = true
)
