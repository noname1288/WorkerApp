package com.example.workerapp.presentation.screens.policy

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workerapp.data.SystemRepository
import com.example.workerapp.data.source.remote.dto.NetworkResult
import com.example.workerapp.data.source.remote.dto.response.PolicyResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PolicyViewModel @Inject constructor(
    private val systemRepository: SystemRepository
): ViewModel(){
    private val _policy = MutableStateFlow<PolicyResponse?>(null)
    val policy = _policy.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    init {
        fetchPolicy()
    }

    fun fetchPolicy(){
        viewModelScope.launch {
            _loading.value = true

            val result = systemRepository.getPolicies()
            when(result){
                is NetworkResult.Error -> {
                    _loading.value = false
                }
                is NetworkResult.Success<PolicyResponse> -> {
                    _policy.value = result.data
                    _loading.value = false
                }
            }
        }
    }

}