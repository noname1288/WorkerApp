package com.example.workerapp.data.model

import com.example.workerapp.data.model.base.UserModel

data class WorkerModel(
    val uid: String,
    val description: String,
    val userDetail: UserModel,
)
