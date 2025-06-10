package com.acteam.vocago.data.remote

import android.util.Log
import com.acteam.vocago.data.model.ApiException
import com.acteam.vocago.data.model.WordDto
import com.acteam.vocago.domain.remote.WordRemoteDataSource
import com.acteam.vocago.utils.VocaGoRoutes
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
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
}