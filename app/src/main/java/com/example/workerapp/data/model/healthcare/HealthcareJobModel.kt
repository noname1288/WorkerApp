package com.example.workerapp.data.model.healthcare

import com.example.workerapp.data.model.healthcare.HealthcareServiceModel
import com.example.workerapp.data.model.cleaning.DurationModel
import com.example.workerapp.data.model.base.JobModel1
import com.example.workerapp.data.model.base.UserModel
import com.example.workerapp.data.repository.remote.dto.wrapper.HealthServiceWrapper
import com.example.workerapp.utils.ServiceType
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class HealthcareJobModel(
    override var uid: String = "",

    override var user: UserModel = UserModel(),
    override var serviceType: String = ServiceType.HealthcareType,

    var services: List<HealthServiceWrapper> = emptyList(),
    override var price: Double = 0.0,
    override var status: String = "",
    override var listDays: List<String> = emptyList(),

    override var createAt: String = "",
    override var startTime: String = "",
    override var location: String = "",

    var shift: ShiftModel = ShiftModel(),

    ) : JobModel1(
    uid,
    user,
    serviceType,
    price,
    status,
    listDays,
    createAt,
    startTime,
    location,
)