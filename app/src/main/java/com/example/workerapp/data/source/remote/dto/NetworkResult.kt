package com.example.workerapp.data.source.remote.dto

sealed class NetworkResult <out T>{
    data class Success<T> (val data: T) : NetworkResult<T>()
    data class Error(val message: String) : NetworkResult<Nothing>()
}