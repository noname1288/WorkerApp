package com.example.workerapp.ui.login

import android.util.Log
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.lifecycle.ViewModel
import com.example.workerapp.utils.locator.AppLocator
import com.example.workerapp.utils.session.UserSession
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.auth.GoogleAuthProvider

class AuthViewModel : ViewModel() {
    //trigger a sign-in with GG button
    val googleIdOption = GetSignInWithGoogleOption.Builder(
        serverClientId = WEB_CLIENT_ID
    ).build()

    val request = GetCredentialRequest.Builder()
        .addCredentialOption(googleIdOption)
        .build()

    fun onGoogleSignInSuccess(result: GetCredentialResponse) {
        //retrieve the credential from the result
        val credential = result.credential

        when (credential) {
            is CustomCredential -> {
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    try {
                        // user Google Id token to validate
                        // and authenticate on the server
                        val googleIdTokenCredential =
                            GoogleIdTokenCredential.createFrom(credential.data)
                        firebaseAuthWithGoogleIdToken(googleIdTokenCredential.idToken)
                    } catch (e: GoogleIdTokenParsingException) {
                        onGoogleSignInError(e)
                    }
                } else {
                    // Catch any unrecognized credential type here.
                    Log.d(TAG, "Unexpected type of credential")
                }
            }

            else -> {
                Log.d(TAG, "Unexpected type of credential")
            }
        }
    }

    fun onGoogleSignInError(exception: Exception) {
        Log.d(TAG, "Unexpected type of credential", exception)
    }

    private fun firebaseAuthWithGoogleIdToken(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)

        AppLocator.firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "FIREBASE: signInWithCredential - success")
                    val user = AppLocator.firebaseAuth.currentUser!!
                    //update User's session
                    UserSession.logIn(
                        uid = user.uid,
                        name = user.displayName ?: "Not found",
                        email = user.email ?: "Not found",
                        profilePicUrl = user.photoUrl.toString()
                    )
                    //updateUI(user)
                } else {
                    Log.e(TAG, "FIREBASE: signInWithCredential - failure", task.exception)
                    //updateUI(null)
                }

            }
            .addOnFailureListener {
                Log.e(TAG, "FIREBASE: signInWithCredential - failure", it)
            }

    }

    companion object {
        private const val TAG = "LoginViewModel"
        private const val WEB_CLIENT_ID =
            "982452710221-c5pev1iv7f4g2a4gv3jg6js1ju84mmbt.apps.googleusercontent.com"
    }
}
