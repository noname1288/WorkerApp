package com.example.workerapp.data.source.model.maintenance

import androidx.room.Embedded
import androidx.room.Relation

data class MaintenanceWithPowers(
    @Embedded val maintenace: MaintenanceServiceModel,
    @Relation(
        parentColumn = "uid",
        entityColumn = "serviceId"
    )
    val power: List<Power>
)
