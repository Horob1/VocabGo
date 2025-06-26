package com.acteam.vocago.data.remote

import com.acteam.vocago.data.model.ApiException
import com.acteam.vocago.data.model.SuccessResponse
import com.acteam.vocago.data.model.UpdateUserDto
import com.acteam.vocago.data.model.UserDto
import com.acteam.vocago.domain.remote.UserRemoteDataSource
import com.acteam.vocago.utils.VocaGoRoutes
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import java.io.File

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

    override suspend fun updateAvatar(avatar: File): Result<UserDto> {
        return try {
            val response = client.put(VocaGoRoutes.GetProfile.path) {
                contentType(ContentType.MultiPart.FormData)
                setBody(
                    MultiPartFormDataContent(
                        formData {
                            append("avatar", avatar.readBytes(), Headers.build {
                                append(HttpHeaders.ContentType, "image/jpg")
                                append(
                                    HttpHeaders.ContentDisposition,
                                    "filename=\"${avatar.name}\""
                                )
                            })
                        }
                    )
                )
            }

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

    override suspend fun updateProfile(updateUserDto: UpdateUserDto): Result<UserDto> {
        return try {
            val response = client.patch(VocaGoRoutes.GetProfile.path) {
                contentType(ContentType.Application.Json)
                setBody(updateUserDto)
            }
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
