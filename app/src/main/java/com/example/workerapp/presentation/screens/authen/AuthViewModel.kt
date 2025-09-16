package com.example.workerapp.ui.authen

import android.util.Log
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workerapp.data.source.remote.NetworkResult
import com.example.workerapp.data.source.remote.dto.request.UserLoginRequest
import com.example.workerapp.data.source.remote.dto.request.UserLoginWithGGRequest
import com.example.workerapp.data.source.remote.dto.request.UserRegisterRequest
import com.example.workerapp.data.source.remote.UserRemoteImpl
import com.example.workerapp.utils.locator.AppLocator
import com.example.workerapp.utils.cached.UserSession
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val userRepository = UserRemoteImpl.getInstance()

    private val _loginState = MutableStateFlow<AuthenticationUIState>(AuthenticationUIState.Idle)
    private val _registerState = MutableStateFlow<AuthenticationUIState>(AuthenticationUIState.Idle)
    val loginState: StateFlow<AuthenticationUIState> = _loginState
    val registerState: StateFlow<AuthenticationUIState> = _registerState

    fun changeLoginState(value: AuthenticationUIState) {
        _loginState.value = value
    }

    fun changeRegisterState(value: AuthenticationUIState) {
        _registerState.value = value
    }

    fun loginWithEmailAndPassword(email: String, password: String) {
        val request = UserLoginRequest(email, password)

        viewModelScope.launch {
            _loginState.value = AuthenticationUIState.Loading

            val result = userRepository.login(request)
            when (result) {
                is NetworkResult.Success -> {
                    //update User's session
                    val currentUser = result.data
                    UserSession.saveState(uid = currentUser.user.uid,
                        displayName = currentUser.user.username,
                        email = currentUser.user.email,
                        profilePicUrl = currentUser.user.avatar,
                        token = currentUser.token)

                    _loginState.value = AuthenticationUIState.Success("Login successful")
                }

                is NetworkResult.Error -> {
                    _loginState.value = AuthenticationUIState.Error(
                        result.message
                    )
                }
            }
        }
    }

    fun registerWithForm(displayName: String, email: String, password: String, avatar: String?) {
        val request = UserRegisterRequest(displayName, email, password, null)

        viewModelScope.launch {
            _registerState.value = AuthenticationUIState.Loading

            val result = userRepository.register(request)
            when (result) {
                is NetworkResult.Success -> {
                    _registerState.value = AuthenticationUIState.Success("Register successful")
                }

                is NetworkResult.Error -> {
                    _registerState.value = AuthenticationUIState.Error(result.message)
                }
            }
        }
    }

    fun clearState() {
        _loginState.value = AuthenticationUIState.Idle
        _registerState.value = AuthenticationUIState.Idle
    }

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

                        /* Authenticate in FirebaseAuth*/
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

                    //get user's token
                    task.result?.user?.getIdToken(true) //force refresh
                        ?.addOnCompleteListener {
                            if (it.isSuccessful) {
                                val firebaseIdToken = it.result?.token
                                Log.d(TAG, "Firebase ID Token: $firebaseIdToken")
                                //call api to save in BE
                                if (firebaseIdToken == null){
                                    _loginState.value = AuthenticationUIState.Error("Firebase ID Token is null")
                                }else{
                                    callApiLoginWithGoogle(firebaseIdToken)
                                }
                            }else {
                                Log.e(TAG, "getIdToken failed", it.exception)
                            }
                        }
                } else {
                    Log.e(TAG, "FIREBASE: signInWithCredential - failure", task.exception)
                    _loginState.value = AuthenticationUIState.Error(task.exception?.message ?: "Firebase authentication failed")
                }

            }
            .addOnFailureListener {
                Log.e(TAG, "FIREBASE: signInWithCredential - failure", it)
                _loginState.value = AuthenticationUIState.Error(it.message ?: "Firebase authentication failed")
            }
    }

    private fun callApiLoginWithGoogle(idToken: String) {
        viewModelScope.launch {
            _loginState.value = AuthenticationUIState.Loading

            val request = UserLoginWithGGRequest(idToken)
            val result = userRepository.loginWithGoogle(request)
            when (result) {
                is NetworkResult.Success -> {
                    //update User's session
                    val currentUser = result.data
                    UserSession.saveState(uid = currentUser.user.uid,
                        displayName = currentUser.user.username,
                        email = currentUser.user.email,
                        profilePicUrl = currentUser.user.avatar,
                        token = currentUser.token)

                    _loginState.value =
                        AuthenticationUIState.Success("Login with Google successful")
                }

                is NetworkResult.Error -> {
                    _loginState.value = AuthenticationUIState.Error(result.message)
                }
            }
        }
    }

    companion object {
        private const val TAG = "AuthViewModel"
        private const val WEB_CLIENT_ID =
            "982452710221-c5pev1iv7f4g2a4gv3jg6js1ju84mmbt.apps.googleusercontent.com"
    }
}

sealed class AuthenticationUIState {
    object Idle : AuthenticationUIState()
    object Loading : AuthenticationUIState()
    data class Success(val message: String) : AuthenticationUIState()
    data class Error(val message: String) : AuthenticationUIState()
}
