package com.acteam.vocago.domain.remote

import com.acteam.vocago.data.model.LoginResponse

interface AuthRemoteDataSource {
    suspend fun login(username: String, password: String): LoginResponse
}