package com.acteam.vocago.domain.remote

import com.acteam.vocago.data.model.LoginGoogleResponse
import com.acteam.vocago.data.model.LoginResponse
import com.acteam.vocago.data.model.VerifyTwoFAResponse

interface AuthRemoteDataSource {
    suspend fun login(username: String, password: String): LoginResponse
    suspend fun forgotPassword(email: String)
    suspend fun resetPassword(email: String, otp: String, password: String)
    suspend fun register(
        username: String, email: String, password: String,
        firstname: String, lastname: String, phoneNumber: String,
        address: String, dob: String, gender: String,
    )

    suspend fun verifyEmail(email: String, otp: String)
    suspend fun resendVerifyEmail(email: String)
    suspend fun verifyTwoFA(email: String, otpToken: String): VerifyTwoFAResponse
    suspend fun loginGoogle(token: String): LoginGoogleResponse
    suspend fun logout(credentialId: String)
}