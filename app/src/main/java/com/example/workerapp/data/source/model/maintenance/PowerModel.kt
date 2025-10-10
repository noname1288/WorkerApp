package com.example.workerapp.data.source.model.maintenance

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass

@Entity(tableName = "power_service")
@JsonClass(generateAdapter = true)
data class PowerModel(
    @PrimaryKey val uid: String,
    val name: String,
    val price: Int,
    val priceAction: Int,
)
