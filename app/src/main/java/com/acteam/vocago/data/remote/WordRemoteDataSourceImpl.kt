package com.acteam.vocago.data.remote

import com.acteam.vocago.data.model.ApiException
import com.acteam.vocago.data.model.SuggestionResponse
import com.acteam.vocago.data.model.WordDto
import com.acteam.vocago.data.model.WordOfTheDaySimpleDto
import com.acteam.vocago.data.model.WordOfTheDaySimpleResponse
import com.acteam.vocago.domain.remote.WordRemoteDataSource
import com.acteam.vocago.utils.VocaGoRoutes
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.HttpStatusCode

class WordRemoteDataSourceImpl(
    private val client: HttpClient,
) : WordRemoteDataSource {
    override suspend fun getWordDetail(word: String): Result<WordDto?> {
        return try {
            val response = client.get(VocaGoRoutes.GetDictionaryWord.path + word)
            when (response.status) {
                HttpStatusCode.OK -> {
                    val data = response.body<WordDto>()
                    Result.success(data)
                }

                else -> {
                    Result.failure(ApiException(response.status.value))
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    override suspend fun getSuggestWord(query: String): Result<List<String>> {
        return try {
            val response = client.get(VocaGoRoutes.GetSuggestWord.path) {
                parameter("q", query)
            }
            when (response.status) {
                HttpStatusCode.OK -> {
                    val data = response.body<SuggestionResponse>()
                    Result.success(data.suggestions)
                }

                else -> Result.failure(ApiException(response.status.value))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getWordOfTheDay(): Result<WordOfTheDaySimpleDto> {
        return try {
            val response = client.get(VocaGoRoutes.GetWordOfTheDay.path)
            if (response.status == HttpStatusCode.OK) {
                val responseBody = response.body<WordOfTheDaySimpleResponse>()
                Result.success(responseBody.data)
            } else {
                Result.failure(ApiException(response.status.value))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}