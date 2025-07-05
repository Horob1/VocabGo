package com.acteam.vocago.data.remote

import com.acteam.vocago.data.model.ApiException
import com.acteam.vocago.data.model.SubmitRequest
import com.acteam.vocago.data.model.SubmitToeicResponse
import com.acteam.vocago.data.model.TestResultDto
import com.acteam.vocago.data.model.TestResultListDto
import com.acteam.vocago.data.model.TestResultResponse
import com.acteam.vocago.data.model.ToeicDetailDto
import com.acteam.vocago.data.model.ToeicDetailResponse
import com.acteam.vocago.data.model.ToeicDto
import com.acteam.vocago.domain.remote.ToeicRemoteDataSource
import com.acteam.vocago.utils.VocaGoRoutes
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType

class ToeicRemoteDataSourceImpl(private val client: HttpClient) : ToeicRemoteDataSource {
    override suspend fun getToeicList(): Result<ToeicDto> {
        return try {
            val response = client.get(VocaGoRoutes.GetToeicList.path)
            when (response.status) {
                HttpStatusCode.OK -> {
                    val data = response.body<ToeicDto>()
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

    override suspend fun getToeicDetail(id: String): Result<ToeicDetailDto> {
        return try {
            val response = client.get(VocaGoRoutes.GetToeicDetail(id).path)
            when (response.status) {
                HttpStatusCode.OK -> {
                    val data = response.body<ToeicDetailResponse>()
                    Result.success(data.data)
                }

                else -> {
                    Result.failure(ApiException(response.status.value))
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return Result.failure(e)
        }
    }

    override suspend fun submitToeicTest(request: SubmitRequest): Result<TestResultDto> {
        return try {
            val response = client.post(VocaGoRoutes.SubmitTest.path) {
                contentType(ContentType.Application.Json)
                setBody(request)
            }

            when (response.status) {
                HttpStatusCode.OK, HttpStatusCode.Created -> {
                    val response = response.body<SubmitToeicResponse>()
                    Result.success(response.data)
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

    override suspend fun getToeicResult(id: String): Result<List<TestResultListDto>> {
        return try {
            val response = client.get(VocaGoRoutes.GetToeicResult(id).path)
            when (response.status) {
                HttpStatusCode.OK -> {
                    val result = response.body<TestResultResponse>()
                    Result.success(result.data)
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

    override suspend fun getToeicResultDetail(id: String): Result<List<TestResultListDto>> {
        return try {
            val response = client.get(VocaGoRoutes.GetToeicResultDetail(id).path)
            when (response.status) {
                HttpStatusCode.OK -> {
                    val result = response.body<TestResultResponse>()
                    Result.success(result.data)
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