package com.acteam.vocago.domain.remote

import com.acteam.vocago.data.model.UpdateUserDto
import com.acteam.vocago.data.model.UserDto
import java.io.File

interface UserRemoteDataSource {
    suspend fun getUser(): Result<UserDto>

    suspend fun updateAvatar(avatar: File): Result<UserDto>

    suspend fun updateProfile(updateUserDto: UpdateUserDto): Result<UserDto>
}