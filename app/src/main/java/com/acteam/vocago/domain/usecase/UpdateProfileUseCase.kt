package com.acteam.vocago.domain.usecase

import com.acteam.vocago.data.model.UpdateUserDto
import com.acteam.vocago.domain.remote.UserRemoteDataSource

class UpdateProfileUseCase(
    private val userRemoteDataSource: UserRemoteDataSource,
) {
    suspend operator fun invoke(updateUserDto: UpdateUserDto) =
        userRemoteDataSource.updateProfile(updateUserDto)
}