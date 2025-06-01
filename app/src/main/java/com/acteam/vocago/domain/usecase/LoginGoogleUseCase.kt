package com.acteam.vocago.domain.usecase

import com.acteam.vocago.data.model.LoginGoogleResponse
import com.acteam.vocago.domain.local.AuthLocalDataSource
import com.acteam.vocago.domain.remote.AuthRemoteDataSource

class LoginGoogleUseCase(
    private val authRemoteDataSource: AuthRemoteDataSource,
    private val authLocalDataSource: AuthLocalDataSource,
) {
    suspend operator fun invoke(token: String): LoginGoogleResponse {
        val data = authRemoteDataSource.loginGoogle(token)
        authLocalDataSource.saveCredential(
            data.accessToken,
            data.refreshToken,
            data.credentialId,
        )
        return data
    }
}