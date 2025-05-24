package com.acteam.vocago.domain.usecase

import com.acteam.vocago.domain.local.AuthLocalDataSource
import kotlinx.coroutines.flow.StateFlow

class GetLoginStateUseCase(
    private val authEncryptedPreferences: AuthLocalDataSource,
) {
    operator fun invoke(): StateFlow<Boolean> {
        return authEncryptedPreferences.isAuth
    }
}