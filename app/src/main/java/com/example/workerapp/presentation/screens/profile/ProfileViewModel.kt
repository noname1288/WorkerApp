package com.example.workerapp.presentation.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workerapp.data.TokenRepository
import com.example.workerapp.data.source.remote.JobRemoteImpl
import com.example.workerapp.data.source.remote.dto.ApplicationDto
import com.example.workerapp.data.source.remote.dto.NetworkResult
import com.example.workerapp.utils.cached.UserSession
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val tokenRepository: TokenRepository,
    private val jobRemoteImpl: JobRemoteImpl
) : ViewModel() {
}

sealed class ApplicationsUiState {
    object Idle : ApplicationsUiState()
    object Loading : ApplicationsUiState()
    data class Success(val data: List<ApplicationDto>) : ApplicationsUiState()
    data class Error(val message: String) : ApplicationsUiState()
}