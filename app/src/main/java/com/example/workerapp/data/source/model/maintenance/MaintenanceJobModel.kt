package com.example.workerapp.data.source.model.maintenance

import com.example.workerapp.data.source.model.base.JobModel1
import com.example.workerapp.data.source.model.base.UserModel
import com.example.workerapp.data.source.remote.dto.wrapper.MaintenanceServiceWrapper
import com.example.workerapp.utils.ServiceType
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MaintenanceJobModel(
    override var uid: String = "",

    override var user: UserModel = UserModel(),
    override var serviceType: String = ServiceType.MaintenanceType,

    override var price: Double = 0.0,
    override var status: String = "",
    override var listDays: List<String> = emptyList(),

    override var createdAt: String = "",
    override var startTime: String = "",
    override var location: String = "",

    var services : List<MaintenanceServiceWrapper> = emptyList()

) : JobModel1 (
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
