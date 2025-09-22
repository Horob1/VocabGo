package com.acteam.vocago.domain.usecase


import com.acteam.vocago.data.model.GetTwoFAQrCodeResponse
import com.acteam.vocago.domain.remote.UserRemoteDataSource

class GetTwoFAQrCodeUseCase(
    private val userRemoteDataSource: UserRemoteDataSource,
) {
    suspend operator fun invoke(): GetTwoFAQrCodeResponse {
        return userRemoteDataSource.getTwoFAQrCode()
    }
}