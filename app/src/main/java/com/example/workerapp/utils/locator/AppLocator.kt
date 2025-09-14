package com.example.workerapp.utils.locator

import com.google.firebase.auth.FirebaseAuth

object AppLocator {
    val firebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }


}