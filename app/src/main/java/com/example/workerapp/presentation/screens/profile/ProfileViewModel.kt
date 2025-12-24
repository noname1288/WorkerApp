package com.example.workerapp.presentation.screens.profile

import androidx.lifecycle.ViewModel
import com.example.workerapp.data.TokenRepository
import com.example.workerapp.data.source.remote.JobRemoteImpl
import com.example.workerapp.data.source.remote.dto.response.ApplicationDto
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val tokenRepository: TokenRepository,
    private val jobRemoteImpl: JobRemoteImpl
) : ViewModel() {
}
