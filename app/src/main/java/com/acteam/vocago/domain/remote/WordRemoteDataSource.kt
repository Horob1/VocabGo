package com.acteam.vocago.domain.remote

import com.acteam.vocago.data.model.WordDto

interface WordRemoteDataSource {
    suspend fun getWordDetail(word: String): Result<WordDto?>
}