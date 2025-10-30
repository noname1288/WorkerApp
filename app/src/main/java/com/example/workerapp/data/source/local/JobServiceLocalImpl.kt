package com.example.workerapp.data.source.local

import com.example.workerapp.data.source.JobServiceDataSource
import com.example.workerapp.data.source.local.room.ServiceDao
import com.example.workerapp.data.source.model.cleaning.CleaningServiceModel
import com.example.workerapp.data.source.model.healthcare.HealthcareServiceModel
import com.example.workerapp.data.source.model.maintenance.MaintenanceServiceModel
import com.example.workerapp.data.source.model.maintenance.PowerModel
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
        serviceDao.insertCleaningService(services)
    }

    override suspend fun saveHealthcareServices(services: List<HealthcareServiceModel>) {
        serviceDao.insertHealthcareService(services)
    }

    override suspend fun getAllMaintenanceServices(): List<MaintenanceServiceModel> =
        serviceDao.getAllMaintenance()

    override suspend fun getMaintenanceServiceByUid(uid: String): MaintenanceServiceModel? =
        serviceDao.getMaintenanceByUid(uid)

    override suspend fun getAllPowers(): List<PowerModel> =
        serviceDao.getAllPowers()

    override suspend fun getPowerByUid(uid: String): PowerModel? =
        serviceDao.getPowerByUid(uid)

    override suspend fun saveMaintenanceService(service: MaintenanceServiceModel) {
        serviceDao.insertMaintenance(service)
    }

    override suspend fun savePowers(powers: List<PowerModel>) {
        serviceDao.insertPowers(powers)
    }

    override suspend fun saveMaintenanceServiceWithPowers(
        service: MaintenanceServiceModel,
        powers: List<PowerModel>
    ) {
        serviceDao.insertMaintenanceServiceWithPowers(service, powers)
    }
}
