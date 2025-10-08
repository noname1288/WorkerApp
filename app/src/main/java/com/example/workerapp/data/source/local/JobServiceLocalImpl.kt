package com.example.workerapp.data.source.local

import com.example.workerapp.data.source.JobServiceDataSource
import com.example.workerapp.data.source.local.room.ServiceDao
import com.example.workerapp.data.source.model.cleaning.CleaningServiceModel
import com.example.workerapp.data.source.model.healthcare.HealthcareServiceModel
import javax.inject.Inject

class JobServiceLocalImpl @Inject constructor(
    private val serviceDao: ServiceDao
) : JobServiceDataSource.Local {
    override suspend fun getCleaningServices(): List<CleaningServiceModel> =
        serviceDao.getCleaningServices()

    override suspend fun getHealthcareServices(): List<HealthcareServiceModel> =
        serviceDao.getHealthcareServices()

    override suspend fun getHealthcareServiceByUid(uid: String): HealthcareServiceModel? =
        serviceDao.getHealthcareServiceByUid(uid)


    override suspend fun saveCleaningServices(services: List<CleaningServiceModel>) {
        services.map { service -> serviceDao.insertCleaningService(service) }
    }

    override suspend fun saveHealthcareServices(services: List<HealthcareServiceModel>) {
        services.map { service -> serviceDao.insertHealthcareService(service) }
    }
}
