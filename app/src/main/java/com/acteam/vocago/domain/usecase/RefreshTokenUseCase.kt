package com.acteam.vocago.domain.usecase

import com.acteam.vocago.data.model.RefreshTokenResponse
import com.acteam.vocago.domain.local.AuthEncryptedPreferences
import com.acteam.vocago.domain.remote.AuthRemoteDataSource

class RefreshTokenUseCase(
    private val authRemoteDataSource: AuthRemoteDataSource,
    private val authLocalDataSource: AuthEncryptedPreferences,
) {
    suspend operator fun invoke(token: String): RefreshTokenResponse {
        val data = authRemoteDataSource.refreshToken(token)
        authLocalDataSource.refreshToken(data.refreshToken, data.accessToken)
        return data
    }
}