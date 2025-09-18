package com.example.workerapp.data.source.local.room

import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

class Converters {
    private val moshi = Moshi.Builder().build()
    private val listAdapter = Types.newParameterizedType(List::class.java, String::class.java)
    private val adapter = moshi.adapter<List<String>>(listAdapter)

    @TypeConverter
    fun fromList(list: List<String>?): String {
        return adapter.toJson(list ?: emptyList())
    }

    @TypeConverter
    fun toList(json: String?): List<String> {
        if (json.isNullOrEmpty()) return emptyList()
        return adapter.fromJson(json) ?: emptyList()
    }
}
