package com.acteam.vocago.domain.usecase

import com.acteam.vocago.domain.remote.AuthRemoteDataSource

class ResendVerifyEmailUseCase(
    private val authRemoteDataSource: AuthRemoteDataSource
) {
    suspend operator fun invoke(
        email: String
    ) {
        return authRemoteDataSource.resendVerifyEmail(email)
    }
}