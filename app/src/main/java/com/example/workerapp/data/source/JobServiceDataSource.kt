package com.example.workerapp.data.source

import com.example.workerapp.data.source.remote.NetworkResult
import com.example.workerapp.data.source.remote.model.cleaning.CleaningServiceModel
import com.example.workerapp.data.source.remote.model.healthcare.HealthcareServiceModel

interface JobServiceDataSource {
    /* *
    * Local
    * */

    /* *
    * Remote
    * */
    interface Remote{
        suspend fun getCleaningServices() : NetworkResult<List<CleaningServiceModel>>
        suspend fun getHealthcareServices() : NetworkResult<List<HealthcareServiceModel>>
    }
}