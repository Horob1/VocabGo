package com.acteam.vocago.domain.remote

import com.acteam.vocago.data.model.UserDto

interface UserRemoteDataSource {
    suspend fun getUser(): Result<UserDto>
}