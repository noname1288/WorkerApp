package com.example.workerapp.utils.cached

object UserSession {
    var isLoggedIn: Boolean = false
    var uid: String? = null
    var displayName: String? = null
    var userEmail: String? = null
    var userProfilePicUrl: String? = null
    var token: String? = null

    fun saveState(uid: String, displayName: String?, email: String?, profilePicUrl: String?, token: String?) {
        isLoggedIn = true
        this.uid = uid
        this.displayName = displayName
        this.userEmail = email
        this.userProfilePicUrl = profilePicUrl
        this.token = token
    }

    fun logOut() {
        isLoggedIn = false
        uid = null
        displayName = null
        userEmail = null
        userProfilePicUrl = null
        token = null
    }
}