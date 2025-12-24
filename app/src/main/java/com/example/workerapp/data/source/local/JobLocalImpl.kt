package com.example.workerapp.data.source.local

import com.example.workerapp.data.error.AppError
import com.example.workerapp.data.source.JobDataSource
import com.example.workerapp.data.source.local.room.ApplicationDao
import com.example.workerapp.data.source.local.room.entity.ApplicationModel
import javax.inject.Inject

class JobLocalImpl @Inject constructor(
    private val applicationDao: ApplicationDao
) : JobDataSource.Local {

    override suspend fun getApplicationsFromLocal(): List<ApplicationModel> {
        return applicationDao.getApplications()
    }

    override suspend fun saveApplicationsToLocal(applications: List<ApplicationModel>) {
        applicationDao.insertListApplications(applications)
    }

    override suspend fun addNewApplicationToLocal(application: ApplicationModel) {
        applicationDao.insertApplication(application)
    }

    override suspend fun getApplicationByJobUid(jobUid: String): Result<List<ApplicationModel>> {
        return runCatching {
            applicationDao.getApplicationsByJobId(jobUid)
        }.recoverCatching { throwable ->
            throw AppError.Database(
                errorMessage = "Fail to get applications from database status following $jobUid "
            )
        }
    }

    override suspend fun updateStatusByApplicationId(
        applicationId: String,
        newStatus: String
    ): Result<Unit> {
        return runCatching {
            applicationDao.updateStatusByApplicationId(applicationId, newStatus)
            Unit
        }.recoverCatching { throwable ->
            throw AppError.Database(
                errorMessage = "Fail to update status: $newStatus for application $applicationId"
            )
        }
    }

    override suspend fun deleteByApplicationId(applicationId: String) {
        applicationDao.deleteByApplicationId(applicationId)
    }

    override suspend fun clearData() {
        applicationDao.clearAll()
    }
}