package com.acteam.vocago.domain.remote

import com.acteam.vocago.data.model.VocaSyncDto

interface VocaRemoteDataSource {
    suspend fun loadImages(word: String): Result<List<String>>

    suspend fun sync(vocaSyncDto: VocaSyncDto): Result<VocaSyncDto>

    suspend fun getVoca(): Result<VocaSyncDto>
}