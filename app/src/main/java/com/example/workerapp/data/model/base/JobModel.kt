package com.example.workerapp.data.model.base

import com.example.workerapp.data.model.cleaning.DurationModel
import com.squareup.moshi.JsonClass

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

@JsonClass(generateAdapter = true)
open class JobModel1(
    open var uid: String,

    open var user: UserModel,
    open var serviceType: String,

    open var workerQuantity: Int,
    open var price: Double,
    open var status: String,
    open var listDays: List<String>,

    open var createAt: String,
    open var startTime: String,
    open var location: String,
)
