package com.acteam.vocago.data.remote

import com.acteam.vocago.data.model.ApiException
import com.acteam.vocago.data.model.SuccessResponse
import com.acteam.vocago.data.model.UserDto
import com.acteam.vocago.domain.remote.UserRemoteDataSource
import com.acteam.vocago.utils.VocaGoRoutes
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode

class UserRemoteDataSourceImpl(
    private val client: HttpClient,
) : UserRemoteDataSource {
    override suspend fun getUser(): Result<UserDto> {
        return try {
            val response = client.get(VocaGoRoutes.GetProfile.path)
            when (response.status) {
                HttpStatusCode.OK -> {
                    val data = response.body<SuccessResponse<UserDto>>()
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
