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
}