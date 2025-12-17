package com.example.workerapp.data

import com.example.workerapp.data.source.local.room.entity.ApplicationModel

interface JobRepository {
    suspend fun getApplications(): Result<List<ApplicationModel>>
}