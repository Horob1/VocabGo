package com.acteam.vocago.domain.usecase

import com.acteam.vocago.domain.remote.UserRemoteDataSource

class ChangePasswordUseCase(
    private val userRemoteDataSource: UserRemoteDataSource,
) {
    suspend operator fun invoke(oldPassword: String, newPassword: String) {
        userRemoteDataSource.changePassword(oldPassword, newPassword)
    }
}