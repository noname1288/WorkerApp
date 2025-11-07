package com.example.workerapp.data.source.model

data class RoomModel(
    val roomId: String = "",
    val users: Map<String, ChatUser> = emptyMap(),
    val lastMessage: String = "",
    val lastTimestamp: Long = 0L,
)

data class ChatUser(
    val username: String = "",
    val avatar: String = ""
)
