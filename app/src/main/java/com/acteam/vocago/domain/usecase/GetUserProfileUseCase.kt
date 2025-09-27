package com.acteam.vocago.domain.usecase

import android.util.Log
import com.acteam.vocago.data.model.UserDto
import com.acteam.vocago.domain.local.UserLocalDataSource
import com.acteam.vocago.domain.remote.UserRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach

class GetLocalUserProfileUseCase(
    private val localDataSource: UserLocalDataSource,
) {
    operator fun invoke(): Flow<UserDto?> =
        localDataSource.getUser()
            .onEach { user ->
                Log.d("GetLocalUserProfileUseCase", "Local user = $user")
            }
}

class SyncProfileUseCase(
    private val localDataSource: UserLocalDataSource,
    private val remoteDataSource: UserRemoteDataSource,
) {
    suspend operator fun invoke(): Result<Unit> {
        return try {
            Log.d("SyncProfileUseCase", "Fetching user from remote...")
            val remoteUser = remoteDataSource.getUser()
            if (remoteUser.isSuccess) {
                val user = remoteUser.getOrNull()
                Log.d("SyncProfileUseCase", "Remote user = $user")
                localDataSource.saveUser(user!!)
                Log.d("SyncProfileUseCase", "User saved to local")
                Result.success(Unit)
            } else {
                Log.e(
                    "SyncProfileUseCase",
                    "Remote error = ${remoteUser.exceptionOrNull()}"
                )
                Result.failure(remoteUser.exceptionOrNull()!!)
            }
        } catch (e: Exception) {
            Log.e("SyncProfileUseCase", "Exception = ${e.message}", e)
            Result.failure(e)
        }
    }
}
