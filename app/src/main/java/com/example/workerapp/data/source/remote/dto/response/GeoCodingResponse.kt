package com.example.workerapp.data.source.remote.dto.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GeoCodingResposne(
    val address: Address,
    val addresstype: String,
    val boundingbox: List<String>,
    val `class`: String,
    val display_name: String,
    val importance: Double,
    val lat: String,
    val licence: String,
    val lon: String,
    val name: String,
    val osm_id: Int,
    val osm_type: String,
    val place_id: Int,
    val place_rank: Int,
    val type: String
)

@JsonClass(generateAdapter = true)
data class Address(
    @Json(name = "ISO3166-2-lvl4")
    val ISO3166_2_lvl4: String,
    val city: String,
    @Json(name = "city_district")
    val cityDistrict: String,
    val country: String,
    @Json(name = "country_code")
    val countryCode: String,
    val postcode: String,
    val road: String
)