package com.example.workerapp.data.source.model.maintenance

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass

@Entity(
    tableName = "power_service",
    foreignKeys = [ForeignKey(
        entity = MaintenanceServiceModel::class,
        parentColumns = ["uid"],
        childColumns = ["serviceId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("serviceId")]
)
@JsonClass(generateAdapter = true)
data class Power(
    @PrimaryKey val uid: String,
    val serviceId: String, //foreign key
    val name: String,
    val price: Int,
    val priceAction: Int,
)
