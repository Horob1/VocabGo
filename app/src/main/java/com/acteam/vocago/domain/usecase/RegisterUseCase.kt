package com.acteam.vocago.domain.usecase

import com.acteam.vocago.domain.remote.AuthRemoteDataSource

class RegisterUseCase(
    private val authRemoteDataSource: AuthRemoteDataSource,
) {
    suspend operator fun invoke(
        username: String,
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        phoneNumber: String,
        address: String,
        dob: String,
        gender: String
    ) {
        return authRemoteDataSource.register(
            username,
            email,
            password,
            firstName,
            lastName,
            phoneNumber,
            address,
            dob,
            gender
        )
    }
}