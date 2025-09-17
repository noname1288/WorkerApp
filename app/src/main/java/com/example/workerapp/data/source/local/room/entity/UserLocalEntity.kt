package com.example.workerapp.data.source.local.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserLocalEntity(
    @PrimaryKey val uid: String = "",
    val username: String = "",
    val gender: String = "",
    val dob: String = "",
    var avatar: String = "",
    var tel: String = "",
    var location: String = "",
    val email: String = "",
    val provider: String = "",
    var emailVerified: Boolean = false,
    var requiresProfileUpdate: Boolean = false,
    val role: String = "",
    var lastLogin: String = "",
)
