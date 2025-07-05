package com.acteam.vocago.domain.usecase

import com.acteam.vocago.domain.remote.UserRemoteDataSource

class LogoutDeviceUseCase(
    private val userRemoteDataSource: UserRemoteDataSource
) {
    suspend operator fun invoke(credentials: List<String>): Result<Unit> {
        return userRemoteDataSource.logoutUserDevice(credentials)
    }
}
