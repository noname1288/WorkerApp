package com.example.workerapp.data.repository

import android.util.Log
import com.example.workerapp.data.JobRepository
import com.example.workerapp.data.source.JobDataSource
import com.example.workerapp.data.source.local.room.entity.ApplicationModel
import com.example.workerapp.data.source.remote.dto.NetworkResult
import com.example.workerapp.data.source.remote.dto.request.CancelApplicationRequest
import com.example.workerapp.data.source.remote.dto.response.ApplicationDto
import com.example.workerapp.data.source.remote.dto.response.toEntity
import com.example.workerapp.utils.cached.UserSession
import javax.inject.Inject

class JobRepositoryImpl @Inject constructor(
    private val local: JobDataSource.Local,
    private val remote: JobDataSource.Remote
) : JobRepository {
    private val TAG = "JobRepositoryImpl"

    override suspend fun getApplications(): Result<List<ApplicationModel>> {
        return try {
            //get applications from remote
            val currentUser = UserSession.uid
            if (currentUser == null)
                return Result.failure(Exception("User not found"))

            val response = remote.getApplication(currentUser)
            when (response) {
                is NetworkResult.Error -> {
                    Result.failure(Exception(response.message))
                }

                is NetworkResult.Success -> {
                    //clear old data
                    local.clearData()

                    //save to local
                    val applicationDtoList = response.data as List<ApplicationDto>
                    val entities = applicationDtoList.map { it.toEntity() }

                    local.saveApplicationsToLocal(entities)
                    Log.d(
                        "JobRepositoryImpl",
                        "getApplications: Saved $entities applications to local"
                    )
                    Result.success(entities)
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Get oldest application by jobUid
     * if not contain, return null
    * */
    override suspend fun getApplicationByJobId(jobUid: String): Result<ApplicationModel?> {
        try {
            val list = local.getApplicationsFromLocal()

            if (list.isEmpty())
                return Result.success(null)

            return Result.success(list[0]) // return first element because the list sorted by 'createdAt'
        }catch (e: Exception){
            return Result.failure(e)
        }
    }

    override suspend fun cancelJob(
        request: CancelApplicationRequest
    ): Result<String> {
        return try {
            val response = remote.cancelApplication(request)
            when (response) {
                is NetworkResult.Error -> {
                    Result.failure(Exception(response.message))
                }

                is NetworkResult.Success -> {
                    Result.success(response.data)
                }
            }
        } catch (e: Exception) {
            Result.failure(Exception(e.message))
        }
    }

    override suspend fun insertApplicationToLocal(application: ApplicationDto): Result<Boolean> {
        return try {
            //map to Application Model
            val applicationEntity = application.toEntity()

            local.addNewApplicationToLocal(applicationEntity)
            Log.d(TAG, "insert new application success: ${applicationEntity.applicationId}")
            Result.success(true)
        }catch (e: Exception){
            Log.d(TAG, "insert new application failure: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun deleteCurrentApplication(applicationId: String): Result<Boolean> {
        return try {
            local.deleteByApplicationId(applicationId)
            Result.success(true)
        }catch (e: Exception){
            Result.failure(e)
        }
    }
}
