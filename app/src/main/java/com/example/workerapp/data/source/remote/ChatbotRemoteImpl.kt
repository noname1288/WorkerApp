package com.example.workerapp.data.source.remote

import android.util.Log
import com.example.workerapp.data.source.ChatbotDataSource
import com.example.workerapp.data.source.remote.api.ChatbotApi
import com.example.workerapp.data.source.remote.dto.NetworkResult
import com.example.workerapp.data.source.remote.dto.request.ChatbotRequest
import com.example.workerapp.data.source.remote.dto.response.ApiErrorResponse
import com.example.workerapp.data.source.remote.dto.response.ChatbotResponse
import com.example.workerapp.data.source.remote.dto.response.GeoCodingResposne
import com.squareup.moshi.Moshi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import okhttp3.Request
import javax.inject.Inject

class ChatbotRemoteImpl @Inject constructor(
    private val chatbotApi: ChatbotApi,
    private val okHttpClient: OkHttpClient,
    private val moshi: Moshi
) : ChatbotDataSource.Remote {

    private val errorAdapter = moshi.adapter(ApiErrorResponse::class.java)

    override suspend fun sendMsg(request: ChatbotRequest): NetworkResult<ChatbotResponse> {
        return try {
            val response = chatbotApi.sendMessage(request)

            if (response.isSuccessful){
                val body = response.body()

                if (body != null){
                    Log.d(TAG, "Body: $body")
                    NetworkResult.Success(body)
                }else {
                    NetworkResult.Error("Empty response body")
                }
            } else {
                val errorMessage = response.errorBody()?.string()
                    ?.let { json -> errorAdapter.fromJson(json)?.error }
                    ?: response.message()
                    ?: "Request failed with status code ${response.code()}"
                Log.e(TAG, errorMessage)

                NetworkResult.Error(errorMessage)
            }
        } catch (e: Exception) {
            NetworkResult.Error(e.localizedMessage ?: "Unexpected error occurred")
        }
    }

    override suspend fun getGeocoding(lat: Double, lon: Double): NetworkResult<GeoCodingResposne> {
        val url = NominatimUtils.getReverseGeocodingUrl(lat, lon)

        return withContext(Dispatchers.IO) {
            try {
                val request = Request.Builder()
                    .url(url)
                    .addHeader("User-Agent", "GoodJobApp/1.0")
                    .build()

                val response = okHttpClient.newCall(request).execute()

                if (!response.isSuccessful) {
                    return@withContext NetworkResult.Error("HTTP ${response.code}")
                }

                val body = response.body?.string()

                if (body.isNullOrEmpty()) {
                    return@withContext NetworkResult.Error("Empty response body")
                }

                // Parse JSON bằng Moshi
                val adapter = moshi.adapter(GeoCodingResposne::class.java)
                val result = adapter.fromJson(body)

                if (result != null) {
                    NetworkResult.Success(result)
                } else {
                    NetworkResult.Error("Failed to parse geocoding response")
                }

            } catch (e: Exception) {
                NetworkResult.Error(e.localizedMessage ?: "Unexpected error occurred")
            }
        }
    }

    companion object{
        private val TAG = "ChatbotRemoteImpl"
    }
}

object NominatimUtils {
    private const val BASE_URL = "https://nominatim.openstreetmap.org"
    private const val ENDPOINT_REVERSE = "/reverse"

    private const val FORMAT = "json"
    private const val ADDRESS_DETAILS = "1"
    private const val ZOOM_LEVEL = "18"

    fun getReverseGeocodingUrl(lat: Double, lon: Double): String {
        return "$BASE_URL$ENDPOINT_REVERSE?" +
                "lat=$lat&lon=$lon" +
                "&format=$FORMAT" +
                "&addressdetails=$ADDRESS_DETAILS" +
                "&zoom=$ZOOM_LEVEL"
    }
}