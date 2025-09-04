package com.example.workerapp.utils.session

object UserSession {
    var isLoggedIn: Boolean = false
    var uid: String? = null
    var displayName: String? = null
    var userEmail: String? = null
    var userProfilePicUrl: String? = null

    fun logIn(uid: String, name: String, email: String, profilePicUrl: String?) {
        isLoggedIn = true
        displayName = name
        userEmail = email
        userProfilePicUrl = profilePicUrl
    }

    fun logOut() {
        isLoggedIn = false
        uid = null
        displayName = null
        userEmail = null
        userProfilePicUrl = null
    }
}
