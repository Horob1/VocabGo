package com.acteam.vocago.domain.local

import com.acteam.vocago.data.model.UserDto

interface UserLocalDataSource {
    suspend fun saveUser(user: UserDto): Result<Unit>
    suspend fun getUser(): UserDto?
    suspend fun clear()
}