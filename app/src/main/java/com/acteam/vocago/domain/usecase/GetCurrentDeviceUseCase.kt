package com.acteam.vocago.domain.usecase

import com.acteam.vocago.domain.local.AuthLocalDataSource

class GetCurrentDeviceUseCase(
    private val authLocalDataSource: AuthLocalDataSource
) {
    operator fun invoke(): String? {
        return authLocalDataSource.getCredentialId()
    }
}
