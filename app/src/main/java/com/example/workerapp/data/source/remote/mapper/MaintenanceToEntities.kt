package com.example.workerapp.data.source.remote.mapper

import com.example.workerapp.data.source.model.maintenance.MaintenanceServiceModel
import com.example.workerapp.data.source.model.maintenance.PowerModel
import com.example.workerapp.data.source.remote.dto.response.MaintenanceServiceResponse

fun mapMaintenanceToEntities(response: MaintenanceServiceResponse) : Pair <MaintenanceServiceModel, List<PowerModel>>{
    val maintenanceServiceEntity = MaintenanceServiceModel(
        uid = response.uid,
        serviceName = response.serviceName,
        serviceType = response.serviceType,
        image = response.image,
        maintenance = response.maintenance,
    )

    val powerEntities = response.powers.map { powerDto ->
        PowerModel(
            uid = powerDto.uid,
            name = powerDto.name,
            price = powerDto.price,
            priceAction = powerDto.priceAction,
        )
    }

    return Pair(maintenanceServiceEntity, powerEntities)
}

