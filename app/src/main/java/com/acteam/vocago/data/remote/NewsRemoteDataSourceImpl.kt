package com.acteam.vocago.data.remote

import com.acteam.vocago.data.model.AnswerNewsRequest
import com.acteam.vocago.data.model.ApiException
import com.acteam.vocago.data.model.NewsDetailDto
import com.acteam.vocago.data.model.NewsDetailLogQsaDto
import com.acteam.vocago.data.model.NewsDto
import com.acteam.vocago.data.model.NewsHistoryDto
import com.acteam.vocago.data.model.PaginatedResponse
import com.acteam.vocago.data.model.SuccessResponse
import com.acteam.vocago.data.model.ToggleBookmarkRequest
import com.acteam.vocago.domain.remote.NewsRemoteDataSource
import com.acteam.vocago.utils.VocaGoRoutes
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType

class NewsRemoteDataSourceImpl(private val client: HttpClient) : NewsRemoteDataSource {
    override suspend fun getNews(
        page: Int,
        limit: Int,
        categories: List<String>,
        keySearch: String,
        level: String,
    ): Result<PaginatedResponse<NewsDto>> {
        return try {
            val response = client.get(VocaGoRoutes.GetNews.path) {
                parameter("page", page)
                parameter("limit", limit)
                if (categories.isNotEmpty()) parameter("categories", categories.joinToString(","))
                if (keySearch.isNotEmpty()) parameter("search", keySearch)
                if (level.isNotEmpty()) parameter("level", level)
            }
            when (response.status) {
                HttpStatusCode.OK -> {
                    val data = response.body<PaginatedResponse<NewsDto>>()
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

    override suspend fun getNewsHistories(
        page: Int,
        limit: Int,
    ): Result<PaginatedResponse<NewsHistoryDto>> {
        return try {
            val response = client.get(VocaGoRoutes.GetNewsHistories.path) {
                parameter("page", page)
                parameter("limit", limit)
            }
            when (response.status) {
                HttpStatusCode.OK -> {
                    val data = response.body<PaginatedResponse<NewsHistoryDto>>()
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

    override suspend fun getNewsDetail(id: String): Result<NewsDetailDto> {
        return try {
            val response = client.get(VocaGoRoutes.GetNews.path + "/$id")
            when (response.status) {
                HttpStatusCode.OK -> {
                    val data = response.body<SuccessResponse<NewsDetailDto>>()
                    Result.success(data.data)
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

    override suspend fun submitPractice(
        newsId: String,
        score: Int,
        questionLogs: List<NewsDetailLogQsaDto>,
    ): Result<Unit> {
        return try {
            val response = client.put(VocaGoRoutes.AnswerNews(id = newsId).path) {
                contentType(ContentType.Application.Json)
                setBody(
                    AnswerNewsRequest(
                        score = score,
                        qsLog = questionLogs
                    )
                )
            }
            when (response.status) {
                HttpStatusCode.OK -> {
                    Result.success(Unit)
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

    override suspend fun toggleBookmark(newsId: String, isBookmarked: Boolean): Result<Unit> {
        return try {
            val response = client.put(VocaGoRoutes.ToggleBookmark(id = newsId).path) {
                contentType(ContentType.Application.Json)
                setBody(
                    ToggleBookmarkRequest(
                        isBookmarked = isBookmarked
                    )
                )
            }

            when (response.status) {
                HttpStatusCode.OK -> {
                    Result.success(Unit)
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