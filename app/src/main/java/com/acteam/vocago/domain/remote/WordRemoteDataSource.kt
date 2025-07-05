package com.acteam.vocago.domain.remote

import com.acteam.vocago.data.model.WordDto
import com.acteam.vocago.data.model.WordOfTheDaySimpleDto

interface WordRemoteDataSource {
    suspend fun getWordDetail(word: String): Result<WordDto?>
    suspend fun getSuggestWord(query: String): Result<List<String>>
    suspend fun getWordOfTheDay(): Result<WordOfTheDaySimpleDto>
}