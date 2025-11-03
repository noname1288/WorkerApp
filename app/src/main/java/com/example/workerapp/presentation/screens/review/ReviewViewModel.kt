package com.example.workerapp.presentation.screens.review

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workerapp.data.ReviewRepository
import com.example.workerapp.data.source.remote.dto.NetworkResult
import com.example.workerapp.data.source.remote.dto.response.ReviewResponse
import com.example.workerapp.utils.cached.UserSession
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReviewViewModel @Inject constructor(
    private val reviewRepository: ReviewRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<ReviewUiState>(ReviewUiState.Idle)
    val uiState: StateFlow<ReviewUiState> = _uiState

    fun fetchReviews(){
        viewModelScope.launch {
            _uiState.value = ReviewUiState.Loading

            val userUid = UserSession.uid

            if (userUid.isNullOrEmpty()){
                _uiState.value = ReviewUiState.Error("User UID is null or empty")
                return@launch
            }

            val result = reviewRepository.getReviews(userUid)

            when(result){
                is NetworkResult.Error -> {
                    _uiState.value = ReviewUiState.Error(result.message ?: "Unknown error")
                }
                is NetworkResult.Success -> {
                    _uiState.value = ReviewUiState.Success(result.data)
                }
            }
        }
    }
}

sealed class ReviewUiState {
    object Idle : ReviewUiState()
    object Loading : ReviewUiState()
    data class Success(val reviews: ReviewResponse) : ReviewUiState()
    data class Error(val message: String) : ReviewUiState()
}
