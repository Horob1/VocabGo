package com.acteam.vocago.data.remote

import android.util.Log
import com.acteam.vocago.data.model.ApiException
import com.acteam.vocago.data.model.GoogleSearchDataResponse
import com.acteam.vocago.data.model.SuccessResponse
import com.acteam.vocago.data.model.VocaSyncDto
import com.acteam.vocago.domain.remote.VocaRemoteDataSource
import com.acteam.vocago.utils.VocaGoRoutes
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType

class VocaRemoteDataSourceImpl(
    private val client: HttpClient,
    private val googleClient: HttpClient,
) : VocaRemoteDataSource {

    override suspend fun loadImages(word: String): Result<List<String>> {
        return try {
            val response = googleClient.get {
                parameter("cx", "14267dd0b04394678")
                parameter("key", "AIzaSyCry0wiDzwereFmOFqSBB-SeJFCQGcJN-k")
                parameter("searchType", "image")
                parameter("q", word)
            }

            Log.d("AAA", response.status.toString())

            when (response.status) {
                HttpStatusCode.OK -> {
                    val data = response.body<GoogleSearchDataResponse>()
                    return Result.success(data.items.map { it.link })
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

    override suspend fun sync(vocaSyncDto: VocaSyncDto): Result<VocaSyncDto> {
        return try {
            val response = client.post(VocaGoRoutes.VocaSync.path) {
                contentType(ContentType.Application.Json)
                setBody(vocaSyncDto)
            }

            when (response.status) {
                HttpStatusCode.OK -> {
                    val data = response.body<SuccessResponse<VocaSyncDto>>()
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

    override suspend fun getVoca(): Result<VocaSyncDto> {
        return try {
            val response = client.get(VocaGoRoutes.GetVoca.path)

            when (response.status) {
                HttpStatusCode.OK -> {
                    val data = response.body<SuccessResponse<VocaSyncDto>>()
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
}