package com.example.workerapp.utils

object ServiceType {
    val CleaningType = "CLEANING"
    val HealthcareType = "HEALTHCARE"
    val MaintenanceType = "MAINTENANCE"

    fun translateToVietnamese(type: String): String {
        return when (type) {
            CleaningType -> "Dịch vụ dọn dẹp"
            HealthcareType -> "Dịch vụ chăm sóc sức khỏe"
            MaintenanceType -> "Dịch vụ bảo trì"
            else -> "Loại dịch vụ không xác định"
        }
    }
}
