package com.acteam.vocago.domain.usecase

import com.acteam.vocago.domain.remote.AuthRemoteDataSource

class ForgotPasswordUseCase(
    private val authRemoteDataSource: AuthRemoteDataSource,
) {
    suspend operator fun invoke(email: String) {
        return authRemoteDataSource.forgotPassword(email)
    }
}