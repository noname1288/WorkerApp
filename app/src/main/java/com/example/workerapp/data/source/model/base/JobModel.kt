package com.example.workerapp.data.source.model.base

import com.squareup.moshi.JsonClass

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
