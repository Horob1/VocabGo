package com.acteam.vocago.domain.remote

import com.acteam.vocago.data.model.LoginResponse

interface AuthRemoteDataSource {
    suspend fun login(username: String, password: String): LoginResponse
    suspend fun forgotPassword(email: String)
    suspend fun resetPassword(email: String, otp: String, password: String)
}