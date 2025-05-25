package com.acteam.vocago.domain.usecase

import com.acteam.vocago.data.model.UserDto
import com.acteam.vocago.domain.local.UserLocalDataSource
import com.acteam.vocago.domain.remote.UserRemoteDataSource
import kotlinx.coroutines.flow.Flow

class GetLocalUserProfileUseCase(
    private val localDataSource: UserLocalDataSource,
) {
    operator fun invoke(): Flow<UserDto?> = localDataSource.getUser()
}

class SyncProfileUseCase(
    private val localDataSource: UserLocalDataSource,
    private val remoteDataSource: UserRemoteDataSource,
) {
    suspend operator fun invoke(): Result<Unit> {
        return try {
            val remoteUser = remoteDataSource.getUser()
            if (remoteUser.isSuccess) {
                localDataSource.saveUser(remoteUser.getOrNull()!!)
                Result.success(Unit)
            } else {
                Result.failure(remoteUser.exceptionOrNull()!!)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }
}

