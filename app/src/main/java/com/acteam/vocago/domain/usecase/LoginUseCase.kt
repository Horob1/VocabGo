package com.acteam.vocago.domain.usecase

import com.acteam.vocago.data.model.LoginResponse
import com.acteam.vocago.domain.local.AuthEncryptedPreferences
import com.acteam.vocago.domain.remote.AuthRemoteDataSource

class LoginUseCase(
    private val authRemoteDataSource: AuthRemoteDataSource,
    private val authLocalDataSource: AuthEncryptedPreferences,
) {
    suspend operator fun invoke(username: String, password: String): LoginResponse {
        val data = authRemoteDataSource.login(username, password)
        authLocalDataSource.saveCredentials(data.accessToken, data.refreshToken, data.credentialId)
        return data
    }
}