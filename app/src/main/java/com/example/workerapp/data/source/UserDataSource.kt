package com.example.workerapp.data.source

import com.example.workerapp.data.source.remote.NetworkResult
import com.example.workerapp.data.source.remote.dto.request.UserLoginRequest
import com.example.workerapp.data.source.remote.dto.request.UserLoginWithGGRequest
import com.example.workerapp.data.source.remote.dto.request.UserRegisterRequest
import com.example.workerapp.data.source.remote.dto.response.UserWrapperResponse

interface UserDataSource {
    /* *
    * Local
    * */

    /* *
    * Remote
    * */
    interface Remote{
        suspend fun login(request: UserLoginRequest): NetworkResult<UserWrapperResponse>
        suspend fun register(request: UserRegisterRequest) : NetworkResult<UserWrapperResponse>
        suspend fun loginWithGoogle(request: UserLoginWithGGRequest): NetworkResult<UserWrapperResponse>
    }
}