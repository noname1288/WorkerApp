package com.example.workerapp.data.source.remote.model

import com.example.workerapp.data.source.remote.model.base.UserModel

data class WorkerModel(
    val uid: String,
    val description: String,
    val userDetail: UserModel,
)
