package com.acteam.vocago.domain.usecase

import com.acteam.vocago.domain.remote.UserRemoteDataSource

class SaveFcmTokenUseCase (
    private val userRemoteDataSource: UserRemoteDataSource
){
    suspend operator fun invoke(fcmToken: String, credentialId: String) {
        userRemoteDataSource.saveFcmToken(fcmToken, credentialId)
    }
}