package com.acteam.vocago.domain.usecase

import com.acteam.vocago.domain.local.AuthLocalDataSource
import com.acteam.vocago.domain.local.UserLocalDataSource
import com.acteam.vocago.domain.remote.AuthRemoteDataSource

class LogoutUseCase(
    private val authLocalDataSource: AuthLocalDataSource,
    private val authRemoteDataSource: AuthRemoteDataSource,
    private val userLocalDataSource: UserLocalDataSource,
) {
    suspend operator fun invoke() {
        val credential = authLocalDataSource.getCredentialId()
        if (credential != null) {
            authRemoteDataSource.logout(credential)
            userLocalDataSource.clear()
            authLocalDataSource.clearTokens()
        } else throw Exception("Some thing was wrong!")
    }
}