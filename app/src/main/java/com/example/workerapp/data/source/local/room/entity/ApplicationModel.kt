package com.example.workerapp.data.source.local.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.workerapp.data.source.remote.dto.response.ApplicationDto

@Entity(tableName = "application_history")
data class ApplicationModel(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val applicationId: String,
    val jobId: String,
    val createdAt: String,
    val serviceType: String,
    val status: String,
)