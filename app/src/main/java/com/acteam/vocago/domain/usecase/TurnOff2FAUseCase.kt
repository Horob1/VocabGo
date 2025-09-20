package com.acteam.vocago.domain.usecase

import com.acteam.vocago.data.model.SetUp2FAResponse
import com.acteam.vocago.domain.remote.UserRemoteDataSource

class TurnOff2FAUseCase(
    private val userRemoteDataSource: UserRemoteDataSource
) {
    suspend operator fun invoke(): SetUp2FAResponse {
        return userRemoteDataSource.disableTwoFA()
    }
}