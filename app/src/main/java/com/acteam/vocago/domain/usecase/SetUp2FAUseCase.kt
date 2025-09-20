package com.acteam.vocago.domain.usecase

import com.acteam.vocago.data.model.SetUp2FAResponse
import com.acteam.vocago.domain.remote.UserRemoteDataSource


class SetUp2FAUseCase(
    private val userRemoteDataSource: UserRemoteDataSource,
) {
    suspend operator fun invoke(otpToken: String): SetUp2FAResponse {
        return userRemoteDataSource.setUp2FA(otpToken)
    }
}