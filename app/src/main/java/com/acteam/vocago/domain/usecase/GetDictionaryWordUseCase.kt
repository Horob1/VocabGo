package com.acteam.vocago.domain.usecase

import com.acteam.vocago.data.model.WordDto
import com.acteam.vocago.domain.remote.WordRemoteDataSource

class GetDictionaryWordUseCase(
    private val wordRemoteDataSource: WordRemoteDataSource,
) {
    suspend operator fun invoke(word: String): Result<WordDto?> =
        wordRemoteDataSource.getWordDetail(word)
}