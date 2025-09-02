package com.example.workerapp.data.model.base

data class UserModel(
    val username: String,
    val gender: String,
    val dob: String,       // có thể đổi sang LocalDate nếu muốn
    val avatar: String,
    val tel: String,
    val location: String,
    val email: String,
    val role: String
)
