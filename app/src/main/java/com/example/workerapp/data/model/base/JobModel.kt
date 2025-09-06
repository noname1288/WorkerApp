package com.example.workerapp.data.model.base

data class  JobModel(
    var serviceType: String = "",
    var startTime: Long = 0L,
    var endTime: Long = 0L,
    var workerQuantity: Int = 0,
    var price: Double = 0.0,
    var isWeek: Boolean = false,
    var dayOfWeek: Int = 0,
    var createdAt: Long = 0L,
    var status: String = ""
)
