package com.example.workerapp.data.repository

import com.example.workerapp.data.JobServiceRepository
import com.example.workerapp.data.source.JobServiceDataSource
import com.example.workerapp.data.source.model.cleaning.CleaningServiceModel
import com.example.workerapp.data.source.model.healthcare.HealthcareServiceModel
import com.example.workerapp.data.source.remote.dto.NetworkResult

class JobServiceRepositoryImpl(
    private val local : JobServiceDataSource.Local,
    private val remote : JobServiceDataSource.Remote
) : JobServiceRepository  {

    override suspend fun getCleaningServices(): Result<List<CleaningServiceModel>> {
        return try {
            val response = remote.getCleaningServices()
            // 1. Gọi API
            when (response) {
                is NetworkResult.Success -> {
                    val services = response.data

                    // 2. Lưu xuống local
                    local.saveCleaningServices(services)

                    // 3. Đọc từ local (đảm bảo dữ liệu đồng bộ)
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
            // 1. Gọi API
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

    companion object{
        const val TAG = "JobServiceRepositoryImpl"

        private var singleton: JobServiceRepositoryImpl? = null

        fun getInstance(
            local: JobServiceDataSource.Local,
            remote: JobServiceDataSource.Remote
        ): JobServiceRepositoryImpl {
            if (singleton == null) {
                singleton = JobServiceRepositoryImpl(local, remote)
            }
            return singleton!!
        }
    }
}