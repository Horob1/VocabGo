package com.acteam.vocago.domain.usecase

import com.acteam.vocago.domain.remote.UserRemoteDataSource
import java.io.File

class UpdateAvatarUseCase(
    private val userRemoteDataSource: UserRemoteDataSource,
) {
    suspend operator fun invoke(avatar: File) {
        userRemoteDataSource.updateAvatar(avatar)
    }
}