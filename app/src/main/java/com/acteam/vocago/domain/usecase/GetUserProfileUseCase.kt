package com.acteam.vocago.domain.usecase

import com.acteam.vocago.data.model.UserDto
import com.acteam.vocago.domain.local.UserLocalDataSource
import com.acteam.vocago.domain.remote.UserRemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class GetUserProfileUseCase(
    private val localDataSource: UserLocalDataSource,
    private val remoteDataSource: UserRemoteDataSource,
) {
    operator fun invoke(): Flow<UserDto?> = flow {
        emit(localDataSource.getUser())

        // Gọi remote
        val remoteData = remoteDataSource.getUser()

        if (remoteData.isSuccess) {
            remoteData.getOrNull().let {
                if (it != null) {
                    localDataSource.saveUser(it)
                    emit(it)
                }
            }
        }
    }.flowOn(Dispatchers.IO)
}