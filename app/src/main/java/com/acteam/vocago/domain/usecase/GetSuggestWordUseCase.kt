package com.acteam.vocago.domain.usecase

import com.acteam.vocago.domain.remote.WordRemoteDataSource

class GetSuggestWordUseCase(
    private val wordRemoteDataSource: WordRemoteDataSource,
) {
    suspend operator fun invoke(query: String): Result<List<String>> =
        wordRemoteDataSource.getSuggestWord(query)
}