package com.example.workerapp.data.source.model.base

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
    open var uid: String = "",

    open var user: UserModel = UserModel(),
    open var serviceType: String = "",

    open var price: Double = 0.0,
    open var status: String = "",
    open var listDays: List<String> = emptyList(),

    open var createdAt: String = "",
    open var startTime: String = "",
    open var location: String = "",
)
