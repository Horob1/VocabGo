package com.acteam.vocago.domain.usecase

import com.acteam.vocago.domain.remote.VocaRemoteDataSource

class LoadImageUseCase(
    private val wordRemoteDataSource: VocaRemoteDataSource,
) {
    suspend operator fun invoke(word: String): List<String> {
        val data = wordRemoteDataSource.loadImages(word).getOrNull()
        return data ?: emptyList()
    }
}