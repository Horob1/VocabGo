package com.acteam.vocago.domain.usecase

import com.acteam.vocago.data.model.WordOfTheDaySimpleDto
import com.acteam.vocago.domain.remote.WordRemoteDataSource

class GetWordOfTheDayUseCase(
    private val wordRemoteDataSource: WordRemoteDataSource
) {
    suspend operator fun invoke(): Result<WordOfTheDaySimpleDto> =
        wordRemoteDataSource.getWordOfTheDay()
}
