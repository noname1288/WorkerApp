package com.example.workerapp.data.repository

import com.example.workerapp.data.JobServiceRepository
import com.example.workerapp.data.source.JobServiceDataSource
import com.example.workerapp.data.source.model.cleaning.CleaningServiceModel
import com.example.workerapp.data.source.model.healthcare.HealthcareServiceModel
import com.example.workerapp.data.source.model.maintenance.MaintenanceServiceModel
import com.example.workerapp.data.source.model.maintenance.PowerModel
import com.example.workerapp.data.source.remote.dto.NetworkResult
import com.example.workerapp.data.source.remote.mapper.mapMaintenanceToEntities
import javax.inject.Inject

class JobServiceRepositoryImpl @Inject constructor(
    private val local: JobServiceDataSource.Local,
    private val remote: JobServiceDataSource.Remote
) : JobServiceRepository {

    override suspend fun getCleaningServices(): Result<List<CleaningServiceModel>> {
        return try {
            val response = remote.getCleaningServices()
            when (response) {
                is NetworkResult.Success -> {
                    val services = response.data

                    // Lưu xuống local
                    local.saveCleaningServices(services)

                    // Đọc từ local (đảm bảo dữ liệu đồng bộ)
                    val cached = local.getCleaningServices()
                    Result.success(cached)
                }

                is NetworkResult.Error -> {
                    // Nếu API fail, fallback dữ liệu local
                    val cached = local.getCleaningServices()
                    if (cached.isNotEmpty()) {
                        Result.success(cached)
                    } else {
                        Result.failure(Exception(response.message))
                    }
                }
            }
        } catch (e: Exception) {
            // Nếu có exception → fallback vào local
            val cached = local.getCleaningServices()
            if (cached.isNotEmpty()) {
                Result.success(cached)
            } else {
                Result.failure(e)
            }
        }
    }

    override suspend fun getHealthcareServices(): Result<List<HealthcareServiceModel>> {
        return try {
            val response = remote.getHealthcareServices()
            when (response) {
                is NetworkResult.Success -> {
                    val services = response.data

                    // 2. Lưu xuống local
                    local.saveHealthcareServices(services)

                    // 3. Đọc từ local
                    val cached = local.getHealthcareServices()
                    Result.success(cached)
                }

                is NetworkResult.Error -> {
                    // Nếu API fail, fallback dữ liệu local
                    val cached = local.getHealthcareServices()
                    if (cached.isNotEmpty()) {
                        Result.success(cached)
                    } else {
                        Result.failure(Exception(response.message))
                    }
                }
            }
        } catch (e: Exception) {
            // Nếu có exception → fallback vào local
            val cached = local.getHealthcareServices()
            if (cached.isNotEmpty()) {
                Result.success(cached)
            } else {
                Result.failure(e)
            }
        }
    }

    override suspend fun getHealthcareServiceByUid(uid: String): Result<HealthcareServiceModel> {
        return try {
            val cached = local.getHealthcareServiceByUid(uid)
            if (cached != null) {
                Result.success(cached)
            } else {
                Result.failure(Exception("Healthcare service $uid not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getMaintenanceServices(): Result<List<MaintenanceServiceModel>> {
        return try{
            val response = remote.getMaintenanceServices()

            when (response) {
                is NetworkResult.Error -> {
                    val cached = local.getAllMaintenanceServices()

                    if (cached.isNotEmpty()) {
                        return Result.success(cached)
                    } else {
                        return Result.failure(Exception(response.message))
                    }
                }

                is NetworkResult.Success -> {
                    val serviceData = response.data

                    serviceData.map { item ->
                        val temp = mapMaintenanceToEntities(item)

//                        // Saving Maintenance Service into local database
//                        local.saveMaintenanceService(temp.first)
//                        //Saving Power Service List into local database
//                        local.savePowers(temp.second)

                        //saving Maintenance Service + Powers using transaction
                        local.saveMaintenanceServiceWithPowers(temp.first, temp.second)
                    }

                    val cached = local.getAllMaintenanceServices()
                    Result.success(cached)
                }
            }
        }catch (e: Exception){
            val cached = local.getAllMaintenanceServices()

            return if (cached.isNotEmpty()) {
                Result.success(cached)
            } else {
                Result.failure(e)
            }
        }
    }

    override suspend fun getMaintenanceServiceByUid(uid: String): Result<MaintenanceServiceModel> {
        return try {
            val cached = local.getMaintenanceServiceByUid(uid)

            if (cached != null){
                Result.success(cached)
            } else {
                Result.failure(Exception("Maintenance service $uid not found"))
            }
        }catch (e: Exception){
            Result.failure(e)
        }
    }

    override suspend fun getPowerModelByUid(uid: String): Result<PowerModel> {
        return try {
            val cached = local.getPowerByUid(uid)

            if (cached != null){
                Result.success(cached)
            } else {
                Result.failure(Exception("Maintenance service $uid not found"))
            }
        }catch (e: Exception){
            Result.failure(e)
        }
    }

    override suspend fun getPowers(): Result<List<PowerModel>> {
        return try {
            val cached = local.getAllPowers()
            if (cached.isNotEmpty()) {
                Result.success(cached)
            } else {
                Result.failure(Exception("No power data found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
