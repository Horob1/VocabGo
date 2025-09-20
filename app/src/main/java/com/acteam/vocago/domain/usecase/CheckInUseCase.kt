package com.acteam.vocago.domain.usecase

import com.acteam.vocago.data.model.CheckInResponse
import com.acteam.vocago.domain.remote.UserRemoteDataSource

class CheckInUseCase(
    private val userRemoteDataSource: UserRemoteDataSource,
) {
    suspend operator fun invoke(): CheckInResponse {
        return userRemoteDataSource.checkIn()
    }
}