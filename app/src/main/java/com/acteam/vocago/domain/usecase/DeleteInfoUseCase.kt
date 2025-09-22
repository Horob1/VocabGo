package com.acteam.vocago.domain.usecase

import com.acteam.vocago.domain.local.AuthLocalDataSource
import com.acteam.vocago.domain.local.UserLocalDataSource

class DeleteInfoUseCase(
    private val userLocalDataSource: UserLocalDataSource,
    private val authLocalDataSource: AuthLocalDataSource,
) {
    suspend operator fun invoke() {
        userLocalDataSource.clear()
        authLocalDataSource.clearTokens()
    }
}