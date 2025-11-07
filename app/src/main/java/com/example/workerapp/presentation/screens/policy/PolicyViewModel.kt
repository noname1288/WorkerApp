package com.example.workerapp.presentation.screens.policy

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workerapp.data.SystemRepository
import com.example.workerapp.data.source.remote.dto.NetworkResult
import com.example.workerapp.data.source.remote.dto.response.PolicyResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PolicyViewModel @Inject constructor(
    private val systemRepository: SystemRepository
) : ViewModel() {

    private val _policy = MutableStateFlow<PolicyResponse?>(null)
    val policy: StateFlow<PolicyResponse?> = _policy

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        fetchPolicies()
    }

    fun fetchPolicies() {
        viewModelScope.launch {
            _loading.value = true
            when (val result = systemRepository.getPolicies()) {
                is NetworkResult.Success -> {
                    _policy.value = result.data
                }
                is NetworkResult.Error -> {
                    _error.value = result.message
                }
            }
            _loading.value = false
        }
    }
}

