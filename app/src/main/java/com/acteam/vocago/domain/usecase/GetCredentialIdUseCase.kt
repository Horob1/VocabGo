package com.acteam.vocago.domain.usecase

import com.acteam.vocago.domain.local.AuthLocalDataSource
import kotlinx.coroutines.flow.StateFlow

class GetCredentialIdUseCase(
    private val authEncryptedPreferences: AuthLocalDataSource,
) {
    operator fun invoke(): StateFlow<String?> {
        return authEncryptedPreferences.credentialIdFlow
    }
}