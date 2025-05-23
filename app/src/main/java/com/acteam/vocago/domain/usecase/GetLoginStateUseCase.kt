package com.acteam.vocago.domain.usecase

import com.acteam.vocago.domain.local.AuthEncryptedPreferences
import kotlinx.coroutines.flow.StateFlow

class GetLoginStateUseCase(
    private val authEncryptedPreferences: AuthEncryptedPreferences,
) {
    operator fun invoke(): StateFlow<Boolean> {
        return authEncryptedPreferences.isAuth
    }
}