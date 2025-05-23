package com.acteam.vocago.domain.usecase

import com.acteam.vocago.data.model.UserDto
import com.acteam.vocago.domain.local.UserLocalDataSource
import com.acteam.vocago.domain.remote.UserRemoteDataSource
import com.acteam.vocago.presentation.screen.common.data.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class GetUserProfileUseCase(
    private val localDataSource: UserLocalDataSource,
    private val remoteDataSource: UserRemoteDataSource,
) {
    operator fun invoke(): Flow<Resource<UserDto>> = flow {
        emit(Resource.Loading)

        val cachedUser = localDataSource.getUser()
        if (cachedUser != null) {
            emit(Resource.Success(cachedUser))
        }

        val remoteResult = remoteDataSource.getUser()
        if (remoteResult.isSuccess) {
            val remoteUser = remoteResult.getOrNull()
            if (remoteUser != null) {
                localDataSource.saveUser(remoteUser)
                emit(Resource.Success(remoteUser))
            } else {
                emit(Resource.Error("No user data from remote", cachedUser))
            }
        } else {
            emit(
                Resource.Error(
                    remoteResult.exceptionOrNull()?.message ?: "Unknown error",
                    cachedUser
                )
            )
        }
    }.flowOn(Dispatchers.IO)
}