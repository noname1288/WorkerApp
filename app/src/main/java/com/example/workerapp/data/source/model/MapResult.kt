package com.example.workerapp.data.source.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class MapResult(
    val address: String,
    val lat: Double,
    val lng: Double
) : Parcelable
