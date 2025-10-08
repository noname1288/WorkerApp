package com.example.workerapp.utils.ext

import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import android.util.Log
import com.example.workerapp.data.source.remote.dto.NetworkResult

suspend fun <T> safeApiCall(
    apiCall: suspend () -> Response<T>,
    tag: String = "Network"
): NetworkResult<T> {
    return withContext(Dispatchers.IO) {
        try {
            val response = apiCall()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Log.d(tag, "Success [${response.code()}]: $body")
                    NetworkResult.Success(body)
                } else {
                    Log.e(tag, "Empty body [${response.code()}]")
                    NetworkResult.Error("Empty response body")
                }
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e(tag, "Server error [${response.code()}]: $errorBody")
                NetworkResult.Error(
                    errorBody ?: "Server error")
            }
        } catch (e: HttpException) {
            val errorMsg = e.response()?.errorBody()?.string() ?: e.message()
            Log.e(tag, "HttpException [${e.code()}]: $errorMsg")
            NetworkResult.Error(errorMsg)
        } catch (e: IOException) {
            Log.e(tag, "Network error: ${e.message}")
            NetworkResult.Error("Network error: ${e.message}")
        } catch (e: Exception) {
            Log.e(tag, "Unexpected error: ${e.message}")
            NetworkResult.Error(e.message ?: "Unknown error")
        }
    }
}
