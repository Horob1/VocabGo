package com.acteam.vocago.domain.usecase

import com.acteam.vocago.data.model.VerifyTwoFAResponse
import com.acteam.vocago.domain.local.AuthLocalDataSource
import com.acteam.vocago.domain.remote.AuthRemoteDataSource

class VerifyTwoFAUseCase(
    private val authRemoteDataSource: AuthRemoteDataSource,
    private val authLocalDataSource: AuthLocalDataSource,
) {
    suspend operator fun invoke(email: String, otpToken: String): VerifyTwoFAResponse {
        val data = authRemoteDataSource.verifyTwoFA(email, otpToken)
        authLocalDataSource.saveCredential(data.accessToken, data.refreshToken, data.credentialId)
        return data
    }

}