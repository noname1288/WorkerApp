package com.example.workerapp.presentation.screens.bot

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workerapp.data.ChatbotRepository
import com.example.workerapp.data.source.model.MessageModel
import com.example.workerapp.data.source.remote.dto.request.ChatbotRequest
import com.example.workerapp.data.source.remote.dto.request.Location
import com.example.workerapp.data.source.remote.dto.request.Reference
import com.example.workerapp.data.source.remote.dto.response.ChatbotJobResponse
import com.example.workerapp.utils.cached.UserSession
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatbotViewModel @Inject constructor(
    private val chatbotRepository: ChatbotRepository,
) : ViewModel() {
    private val _messageList = MutableStateFlow<List<MessageModel>>(emptyList())
    val messageList = _messageList.asStateFlow()

    private val _chatbotResponse = MutableStateFlow<List<ChatbotResponseUiModel>>(emptyList())
    val chatbotResponse = _chatbotResponse.asStateFlow()

    val addressName = MutableStateFlow<String>("")
    val addressLat = MutableStateFlow<Double>(0.0)
    val addressLon = MutableStateFlow<Double>(0.0)

    init {
        viewModelScope.launch {
            val currentUserId = UserSession.uid ?: ""

            if (currentUserId.isEmpty()) {
                return@launch
            }

            val currentTimestamp = System.currentTimeMillis()

            val mockMessage = ChatbotResponseUiModel.TextResponse(
                "chatbot",
                text = "Xin chào! Tôi là trợ lý AI của GoodJob. Tôi có thể giúp bạn tìm công việc, tìm hiểu về dịch vụ hoặc thông tin về ứng dụng. Bạn cần hỗ trợ gì?"
            )

            _chatbotResponse.update { oldList -> oldList + mockMessage }
        }
    }

    fun updateCurrentLocation(name: String, lat: Double, lon: Double) {
        addressLat.value = lat
        addressLon.value = lon
        Log.d(TAG, "lat: ${addressLat.value} | lon: ${addressLon.value}")

        viewModelScope.launch {
            val result = chatbotRepository.getGeoCoding(lat, lon)

            result.onSuccess { data ->
                addressName.value = data.display_name
                Log.d(TAG, "name: ${addressName.value}")
            }.onFailure {
                addressName.value = ""
            }

        }
    }

    fun sendMessage(query: String) {
        //check usersession
        val currentUserUid = UserSession.uid ?: return

        // add user's message
        val userMessage = ChatbotResponseUiModel.TextResponse(currentUserUid, query)
        _chatbotResponse.update { oldList -> oldList + userMessage }

        viewModelScope.launch {
            val currentLocation = Location(
                name = addressName.value,
                lat = addressLat.value,
                lon = addressLon.value
            )

            val reference = Reference(
                location = currentLocation
            )

            val request = ChatbotRequest(
                query = query,
                reference = reference
            )

            val result = chatbotRepository.sendMessage(request)
            result.onSuccess { data ->
                _chatbotResponse.update { oldList -> oldList + data }
            }.onFailure { error ->
                Log.e(TAG, error.message ?: "something wrong")
            }
        }
    }

    companion object {
        private val TAG = "ChatbotViewModel"
    }
}

sealed class ChatbotResponseUiModel {
    data class JobResponse(val text: String, val listJobs: List<ChatbotJobResponse>) :
        ChatbotResponseUiModel()

    data class TextResponse(val userUid: String, val text: String) : ChatbotResponseUiModel()
}

