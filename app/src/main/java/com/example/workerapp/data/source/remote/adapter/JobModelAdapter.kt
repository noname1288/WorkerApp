package com.example.workerapp.data.source.remote.adapter

import com.example.workerapp.data.source.model.base.JobModel1
import com.example.workerapp.data.source.model.cleaning.CleaningJobModel1
import com.example.workerapp.data.source.model.healthcare.HealthcareJobModel
import com.example.workerapp.data.source.model.maintenance.MaintenanceJobResponse
import com.example.workerapp.utils.ServiceType
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

class JobModelAdapter(
    private val moshi: Moshi
) : JsonAdapter<JobModel1>() {

    override fun fromJson(reader: JsonReader): JobModel1? {
        val jsonValue = reader.readJsonValue() as? Map<*, *> ?: return null
        val serviceType = jsonValue["serviceType"] as? String ?: return null

        return when (serviceType) {
            ServiceType.CleaningType -> moshi.adapter(CleaningJobModel1::class.java)
                .fromJsonValue(jsonValue)

            ServiceType.HealthcareType -> moshi.adapter(HealthcareJobModel::class.java)
                .fromJsonValue(jsonValue)

            ServiceType.MaintenanceType -> moshi.adapter(MaintenanceJobResponse::class.java)
                .fromJsonValue(jsonValue)

            else -> null
        }
    }

    override fun toJson(writer: JsonWriter, value: JobModel1?) {
        when (value) {
            is CleaningJobModel1 -> writer.jsonValue(value)
            is HealthcareJobModel -> writer.jsonValue(value)
            is MaintenanceJobResponse -> writer.jsonValue(value)
            else -> writer.nullValue()
        }
    }

    companion object {
        val FACTORY = Factory { type, annotations, moshi ->
            if (Types.getRawType(type) == JobModel1::class.java) {
                JobModelAdapter(moshi)
            } else null
        }
    }
}

