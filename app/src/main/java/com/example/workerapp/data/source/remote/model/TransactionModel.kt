package com.example.workerapp.data.source.remote.model

data class TransactionModel (
    val id: String,
    val date: Long,
    val amount: Double,
    val description: String
)
