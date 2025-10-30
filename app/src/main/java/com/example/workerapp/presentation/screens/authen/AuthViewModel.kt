package com.example.workerapp.presentation.screens.authen

import android.util.Log
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workerapp.data.TokenRepository
import com.example.workerapp.data.UserRepository
import com.example.workerapp.data.source.local.room.entity.UserLocalEntity
import com.example.workerapp.data.source.remote.dto.request.ChangePasswordRequest
import com.example.workerapp.data.source.remote.dto.request.UserLoginRequest
import com.example.workerapp.data.source.remote.dto.request.UserLoginWithGGRequest
import com.example.workerapp.data.source.remote.dto.request.UserRegisterRequest
import com.example.workerapp.session.SessionManager
import com.example.workerapp.utils.cached.UserSession
import com.example.workerapp.utils.locator.AppLocator
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val tokenRepository: TokenRepository,
    private val userRepository: UserRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _loginUiState = MutableStateFlow<AuthenticationUIState>(AuthenticationUIState.Idle)
    val loginUiState: StateFlow<AuthenticationUIState> = _loginUiState

    private val _registerUiState =
        MutableStateFlow<AuthenticationUIState>(AuthenticationUIState.Idle)
    val registerUiState: StateFlow<AuthenticationUIState> = _registerUiState

    private val _splashState = MutableStateFlow<AuthenticationUIState>(AuthenticationUIState.Idle)
    val splashState: StateFlow<AuthenticationUIState> = _splashState

    private val _changePasswordUiState =
        MutableStateFlow<AuthenticationUIState>(AuthenticationUIState.Idle)
    val changePasswordUiState: StateFlow<AuthenticationUIState> = _changePasswordUiState

    val loggedIn = sessionManager.isLoggedIn
    val tokenExpiredEvent = sessionManager.tokenExpired

    fun checkLoginStatus() {
        viewModelScope.launch {
            sessionManager.checkLoginStatus()

            if (loggedIn.value) {
                val user = userRepository.getUserProfile().firstOrNull()?.let {
                    _splashState.value = AuthenticationUIState.Success(it)
                    UserSession.saveState(it.uid, it.username, it.email, it.avatar)
                }
            } else {
                _splashState.value = AuthenticationUIState.Error("Not logged in")
            }
        }
    }

    fun loginWithEmailAndPassword(email: String, password: String) {
        val request = UserLoginRequest(email, password)

        viewModelScope.launch {
            _loginUiState.value = AuthenticationUIState.Loading

            val result = userRepository.login(request)

            result.onSuccess {
                _loginUiState.value = AuthenticationUIState.Success(it)

                //update session
                UserSession.saveState(it.uid, it.username, it.email, it.avatar)

                //update fcm token
                val fcmTokenLocal = tokenRepository.getFcmToken()
                if (fcmTokenLocal != null) {
                    userRepository.saveFcmToken(fcmTokenLocal)
                }
            }.onFailure {
                _loginUiState.value =
                    AuthenticationUIState.Error(it.message ?: "Login failed")
            }
        }
    }

    fun registerWithForm(
        displayName: String,
        email: String,
        password: String,
        avatar: String?
    ) {
        val request = UserRegisterRequest(displayName, email, password, null)

        viewModelScope.launch {
            _registerUiState.value = AuthenticationUIState.Loading

            val result = userRepository.register(request)

            result.onSuccess {
                _registerUiState.value = AuthenticationUIState.Success(it)

                //update session
                UserSession.saveState(it.uid, it.username, it.email, it.avatar)

                //update fcm token
                val fcmTokenLocal = tokenRepository.getFcmToken()
                if (fcmTokenLocal != null) {
                    userRepository.saveFcmToken(fcmTokenLocal)
                }
            }.onFailure {
                _registerUiState.value =
                    AuthenticationUIState.Error(it.message ?: "Registration failed")
            }
        }
    }




    fun clearState() {
        _loginUiState.value = AuthenticationUIState.Idle
        _registerUiState.value = AuthenticationUIState.Idle
        _splashState.value = AuthenticationUIState.Idle
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

    fun logout() {
        viewModelScope.launch {
            //clear user session
            UserSession.logOut()

            //clear token in local storage
            sessionManager.logout()

            //clear user profile in local database
            userRepository.clearUserProfile()
        }
    }

    private fun firebaseAuthWithGoogleIdToken(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)

        AppLocator.firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "FIREBASE: signInWithCredential - success")

                    //get user's token
                    task.result?.user?.getIdToken(true) //force refresh
                        ?.addOnCompleteListener {
                            if (it.isSuccessful) {
                                val firebaseIdToken = it.result?.token
                                Log.d(TAG, "Firebase ID Token: $firebaseIdToken")
                                //call api to save in BE
                                if (firebaseIdToken == null) {
                                    _loginUiState.value =
                                        AuthenticationUIState.Error("Firebase ID Token is null")
                                } else {
                                    callApiLoginWithGoogle(firebaseIdToken)
                                }
                            } else {
                                Log.e(TAG, "getIdToken failed", it.exception)
                            }
                        }
                } else {
                    Log.e(TAG, "FIREBASE: signInWithCredential - failure", task.exception)
                    _loginUiState.value = AuthenticationUIState.Error(
                        task.exception?.message ?: "Firebase authentication failed"
                    )
                }

            }
            .addOnFailureListener {
                Log.e(TAG, "FIREBASE: signInWithCredential - failure", it)
                _loginUiState.value =
                    AuthenticationUIState.Error(it.message ?: "Firebase authentication failed")
            }
    }

    private fun callApiLoginWithGoogle(idToken: String) {
        viewModelScope.launch {
            _loginUiState.value = AuthenticationUIState.Loading

            val request = UserLoginWithGGRequest(idToken)
            val result = userRepository.loginWithGoogle(request)

            result.onSuccess {
                _loginUiState.value = AuthenticationUIState.Success(it)

                //update session
                UserSession.saveState(it.uid, it.username, it.email, it.avatar)

                //update fcm token
                val fcmTokenLocal = tokenRepository.getFcmToken()
                if (fcmTokenLocal != null) {
                    userRepository.saveFcmToken(fcmTokenLocal)
                }
            }.onFailure {
                _loginUiState.value =
                    AuthenticationUIState.Error(it.message ?: "Login with Google failed")
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
    data class Success(val userProfile: UserLocalEntity) : AuthenticationUIState()
    data class Error(val message: String) : AuthenticationUIState()
}
