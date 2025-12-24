package com.example.workerapp.data.source.remote.dto

sealed class NetworkResult <out T>{
    data class Success<T> (val data: T) : NetworkResult<T>()
    data class Error(val message: String) : NetworkResult<Nothing>()
}

inline fun <T> NetworkResult<T>.toResult(): Result<T> =
    when (this) {
        is NetworkResult.Success -> Result.success(data)
        is NetworkResult.Error -> Result.failure(Exception(message))
    }