package com.acteam.vocago.domain.usecase


import com.acteam.vocago.domain.remote.AuthRemoteDataSource

class VerifyEmailUseCase(
    private val authRemoteDataSource: AuthRemoteDataSource,
) {
    suspend operator fun invoke(email: String, otp: String) {
        return authRemoteDataSource.verifyEmail(email, otp)
    }
}
