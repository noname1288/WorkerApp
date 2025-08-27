package com.example.workerapp.data.model

import com.example.workerapp.data.model.base.ShiftModel

data class DurationModel(
    val id: String,
    val description: String,

    val shiftDetail: ShiftModel
)
