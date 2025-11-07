package com.example.workerapp.data.source.model

data class MessageModel(
    val id: String = "",
    val message: String = "",
    val receiverId: String = "",
    val senderId: String = "",
    val timestamp: Long = 0L,
)