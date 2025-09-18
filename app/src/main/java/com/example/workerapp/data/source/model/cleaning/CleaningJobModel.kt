package com.example.workerapp.data.source.model.cleaning

import com.example.workerapp.data.source.model.base.JobModel1
import com.example.workerapp.data.source.model.base.UserModel
import com.example.workerapp.utils.ServiceType
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CleaningJobModel1(
    override var uid: String = "",

    override var user: UserModel = UserModel(),
    override var serviceType: String = ServiceType.CleaningType,

    override var price: Double = 0.0,
    override var status: String = "",
    override var listDays: List<String> = emptyList(),

    override var createdAt: String = "",
    override var startTime: String = "",
    override var location: String = "",

    var duration: DurationModel = DurationModel(),

    var isCooking: Boolean = false,
    var isIroning: Boolean = false,
) : JobModel1(
    uid,
    user,
    serviceType,
    price,
    status,
    listDays,
    createdAt,
    startTime,
    location,
)
