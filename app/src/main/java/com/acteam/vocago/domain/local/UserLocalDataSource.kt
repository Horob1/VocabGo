package com.acteam.vocago.domain.local

import com.acteam.vocago.data.model.UserDto
import kotlinx.coroutines.flow.Flow

interface UserLocalDataSource {
    suspend fun saveUser(user: UserDto): Result<Unit>
    fun getUser(): Flow<UserDto?>
    suspend fun clear()
}