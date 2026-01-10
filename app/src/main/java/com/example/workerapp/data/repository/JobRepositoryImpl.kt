package com.example.workerapp.data.repository

import android.util.Log
import com.example.workerapp.data.JobRepository
import com.example.workerapp.data.error.AppError
import com.example.workerapp.data.source.JobDataSource
import com.example.workerapp.data.source.local.room.entity.ApplicationModel
import com.example.workerapp.data.source.remote.dto.request.ApplicationRequest
import com.example.workerapp.data.source.remote.dto.request.CancelApplicationRequest
import com.example.workerapp.data.source.remote.dto.response.ApplicationDto
import com.example.workerapp.data.source.remote.dto.response.toApplicationModel
import com.example.workerapp.utils.ApplicationStatusType
import com.example.workerapp.utils.cached.UserSession
import javax.inject.Inject

class JobRepositoryImpl @Inject constructor(
    private val local: JobDataSource.Local,
    private val remote: JobDataSource.Remote
) : JobRepository {
    private val TAG = "JobRepositoryImpl"

    override suspend fun syncApplicationsFromRemote(): Result<List<ApplicationModel>> {
        return runCatching {
            //get current user
            val currentUser = UserSession.requireUserId().getOrThrow()

            //get applications from remote
            val applicationDtoList = remote.getApplication(currentUser).getOrThrow()
            val entities = applicationDtoList.map { it.toApplicationModel() }
            Log.d(TAG, "$entities")

            //upsert to local
            local.upsertApplicationToLocal(entities)

            entities
        }
    }

    /**
     * Get oldest application by jobUid
     * if not contain, return null
     * */
    override suspend fun getApplicationByJobId(jobUid: String): Result<ApplicationModel?> {
        return runCatching {
            val applicationList = local.getApplicationByJobUid(jobUid).getOrThrow()

            if (applicationList.isEmpty())
                null
            else {
                val lastestItem = applicationList.getOrNull(0)

                if (lastestItem == null)
                    throw AppError.Business("Error to fetch lastest application following $jobUid")

                lastestItem
            }
        }
    }

    /**
     *
     *
     * */
    override suspend fun cancelJob(
        request: CancelApplicationRequest
    ): Result<Unit> {
        return runCatching {
            //remote.cancel job
            val applicationWrapper = remote.cancelApplication(request).getOrThrow()

            if (applicationWrapper == null)
                throw AppError.Business("Something wrong: Application is null")

            //update local
            local.updateStatusByApplicationId(
                applicationWrapper.uid,
                ApplicationStatusType.CANCEL
            ).getOrThrow()
        }
    }

    /**
     *
     *
     * */
    override suspend fun insertApplicationToLocal(application: ApplicationDto): Result<Boolean> {
        return try {
            //map to Application Model
            val applicationEntity = application.toApplicationModel()

            local.addNewApplicationToLocal(applicationEntity)
            Log.d(TAG, "insert new application success: ${applicationEntity.applicationId}")
            Result.success(true)
        } catch (e: Exception) {
            Log.d(TAG, "insert new application failure: ${e.message}")
            Result.failure(e)
        }
    }

    /**
     *
     *
     * */
    override suspend fun updateStatusByApplicationId(
        applicationId: String,
        newStatus: String
    ): Result<Unit> {
        return try {
            local.updateStatusByApplicationId(applicationId, newStatus)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     *
     *
     * */
    override suspend fun deleteCurrentApplication(applicationId: String): Result<Boolean> {
        return try {
            local.deleteByApplicationId(applicationId)
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     *
     *
     * */
    override suspend fun applyJob(request: ApplicationRequest): Result<Unit> =
        runCatching {
            //1. apply job (generate a new application but BE not response the application for you)
            remote.applyForJob(request).getOrThrow()

            //2. get current user
            val userId = UserSession.requireUserId().getOrThrow()

            //3. fetch the lastest application
            val lastestApplication =
                remote.getApplication(userId).getOrThrow().firstOrNull() ?: throw AppError.Business(
                    "lastest application is null"
                )

            //4. save to local
            local.addNewApplicationToLocal(lastestApplication.toApplicationModel() /*convert to ApplicationModel*/)
        }
}
