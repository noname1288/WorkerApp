package com.example.workerapp.data.repository

import android.util.Log
import com.example.workerapp.data.JobRepository
import com.example.workerapp.data.source.JobDataSource
import com.example.workerapp.data.source.local.room.entity.ApplicationModel
import com.example.workerapp.data.source.remote.dto.NetworkResult
import com.example.workerapp.data.source.remote.dto.response.ApplicationDto
import com.example.workerapp.data.source.remote.dto.response.toEntity
import com.example.workerapp.utils.cached.UserSession
import javax.inject.Inject

class JobRepositoryImpl @Inject constructor(
    private val local: JobDataSource.Local,
    private val remote: JobDataSource.Remote
) : JobRepository {
    override suspend fun getApplications(): Result<List<ApplicationModel>> {
        return try {
            //get applications from remote
            val currentUser = UserSession.uid
            if (currentUser == null)
                return Result.failure(Exception("User not found"))

            val response = remote.getApplication(currentUser)
            when(response){
                is NetworkResult.Error -> {
                    Result.failure(Exception(response.message))
                }
                is NetworkResult.Success -> {
                    //save to local
                    val applicationDtoList = response.data as List<ApplicationDto>
                    val entities = applicationDtoList.map { it.toEntity() }

                    local.saveApplicationsToLocal(entities)
                    Log.d("JobRepositoryImpl", "getApplications: Saved $entities applications to local")
                     Result.success(entities)
                }
            }
        }catch (e : Exception){
             Result.failure(e)
        }
    }
}