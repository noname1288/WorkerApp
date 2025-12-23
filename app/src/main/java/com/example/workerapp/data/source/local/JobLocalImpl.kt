package com.example.workerapp.data.source.local

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

    override suspend fun checkApplicationByJobUid(jobUid: String): List<ApplicationModel> {
        val entities = applicationDao.getApplicationsByJobId(jobUid)

        return entities
    }

    override suspend fun deleteByApplicationId(applicationId: String) {
        applicationDao.deleteByApplicationId(applicationId)
    }

    override suspend fun clearData() {
        applicationDao.clearAll()
    }
}