package com.acteam.vocago.domain.remote

interface VocaRemoteDataSource {
    suspend fun loadImages(word: String): Result<List<String>>
}