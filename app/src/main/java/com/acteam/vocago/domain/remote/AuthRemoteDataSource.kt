package com.acteam.vocago.domain.remote

interface AuthRemoteDataSource {
    suspend fun login(username: String, password: String)
}