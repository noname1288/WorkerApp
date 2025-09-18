package com.example.workerapp.utils.cached

import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object UserSession {
    private var _isLoggedIn = MutableStateFlow(true)
    val isLoggedIn : StateFlow<Boolean> = _isLoggedIn

    var uid: String? = null
    var displayName: String? = null
    var userEmail: String? = null
    var userProfilePicUrl: String? = null

    fun saveState(uid: String, displayName: String?, email: String?, profilePicUrl: String?) {
        Log.d("UserSession", "saveState: $uid, $displayName, $email, $profilePicUrl ")
        _isLoggedIn.value = true

        this.uid = uid
        this.displayName = displayName
        this.userEmail = email
        this.userProfilePicUrl = profilePicUrl
    }

    fun logOut() {
        _isLoggedIn.value = false

        uid = null
        displayName = null
        userEmail = null
        userProfilePicUrl = null
    }
}