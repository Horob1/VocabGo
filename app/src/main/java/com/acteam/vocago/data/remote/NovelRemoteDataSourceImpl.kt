package com.acteam.vocago.data.remote

import com.acteam.vocago.data.model.ApiException
import com.acteam.vocago.data.model.NovelDetailDto
import com.acteam.vocago.data.model.NovelDto
import com.acteam.vocago.data.model.PaginatedResponse
import com.acteam.vocago.data.model.SuccessResponse
import com.acteam.vocago.domain.model.Novel
import com.acteam.vocago.domain.remote.NovelRemoteDataSource
import com.acteam.vocago.utils.VocaGoRoutes
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.HttpStatusCode

class NovelRemoteDataSourceImpl(
    private val client: HttpClient,
) : NovelRemoteDataSource {
    override suspend fun getFirstPageOnline(): Result<List<Novel>> {
        return try {
            val response = client.get(VocaGoRoutes.GetNovel.path) {
                parameter("page", 1)
                parameter("limit", 5)
            }

            when (response.status) {
                HttpStatusCode.OK -> {
                    val data = response.body<PaginatedResponse<NovelDto>>()
                    Result.success(data.data.map { it.toNovel() })
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

    override suspend fun getNovelList(
        page: Int,
        keySearch: String,
    ): Result<PaginatedResponse<NovelDto>> {
        return try {
            val response = client.get(VocaGoRoutes.GetNovel.path) {
                parameter("page", page)
                parameter("limit", 5)
                if (keySearch.isNotEmpty()) parameter("keySearch", keySearch)
            }

            when (response.status) {
                HttpStatusCode.OK -> {
                    val data = response.body<PaginatedResponse<NovelDto>>()
                    return Result.success(data)
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

    override suspend fun getNovelDetail(id: String): Result<NovelDetailDto> {
        return try {
            val response = client.get(VocaGoRoutes.GetNovel.path + "/$id")

            when (response.status) {
                HttpStatusCode.OK -> {
                    val data = response.body<SuccessResponse<NovelDetailDto>>()
                    return Result.success(data.data)
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