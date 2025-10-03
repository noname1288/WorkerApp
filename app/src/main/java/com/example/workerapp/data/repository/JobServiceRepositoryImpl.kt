package com.example.workerapp.data.repository

import com.example.workerapp.data.JobServiceRepository
import com.example.workerapp.data.source.JobServiceDataSource
import com.example.workerapp.data.source.model.cleaning.CleaningServiceModel
import com.example.workerapp.data.source.model.healthcare.HealthcareServiceModel
import com.example.workerapp.data.source.remote.dto.NetworkResult
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
}
