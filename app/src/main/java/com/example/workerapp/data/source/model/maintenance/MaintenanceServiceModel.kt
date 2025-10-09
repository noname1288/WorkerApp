package com.example.workerapp.data.source.model.maintenance

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.workerapp.utils.ServiceType
import com.squareup.moshi.JsonClass

@Entity(tableName = "maintenance_service")
@JsonClass(generateAdapter = true)
data class MaintenanceServiceModel(
    @PrimaryKey val uid: String,
    val serviceName: String,
    val serviceType: String = ServiceType.MaintenanceType,
    val image: String,
    val maintenance: String,
)
