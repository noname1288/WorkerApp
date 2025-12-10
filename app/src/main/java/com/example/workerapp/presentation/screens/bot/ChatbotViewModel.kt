package com.example.workerapp.presentation.screens.bot

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.util.query
import com.example.workerapp.data.ChatbotRepository
import com.example.workerapp.data.source.model.MessageModel
import com.example.workerapp.data.source.remote.dto.request.ChatbotRequest
import com.example.workerapp.data.source.remote.dto.request.Location
import com.example.workerapp.data.source.remote.dto.request.Reference
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
) : ViewModel(){
    private val _messageList = MutableStateFlow<List<MessageModel>>(emptyList())
    val messageList = _messageList.asStateFlow()

    val addressName = MutableStateFlow<String>("")
    val addressLat = MutableStateFlow<Double>(0.0)
    val addressLon = MutableStateFlow<Double>(0.0)

    init {
        viewModelScope.launch {
            val currentUserId = UserSession.uid ?: ""

            if (currentUserId.isEmpty()){
                return@launch
            }

            val currentTimestamp = System.currentTimeMillis()

            val mockMessage = MessageModel(
                id = "0",
                message= "Chao ban",
                receiverId = currentUserId,
                senderId = "hello-chatbot",
                timestamp = currentTimestamp
            )

            _messageList.update { oldList -> oldList + mockMessage }
        }
    }

    fun updateCurrentLocation(name: String, lat: Double, lon: Double){
        addressName.value = name
        addressLat.value = lat
        addressLon.value = lon
    }

    fun sendMessage(query: String){
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

            chatbotRepository.sendMessage(request)
        }
    }
}
