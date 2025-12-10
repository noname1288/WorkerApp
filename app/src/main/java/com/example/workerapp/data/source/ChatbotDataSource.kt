package com.example.workerapp.data.source

import com.example.workerapp.data.source.remote.dto.NetworkResult
import com.example.workerapp.data.source.remote.dto.request.ChatbotRequest
import com.example.workerapp.data.source.remote.dto.response.ChatbotResponse

interface ChatbotDataSource {
    /* *
    * Local
    * */

    /* *
    * Remote
    * */
    interface Remote{
        suspend fun sendMsg(request: ChatbotRequest) : NetworkResult<ChatbotResponse>
    }
}