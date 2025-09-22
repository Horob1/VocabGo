package com.acteam.vocago.data.remote

import com.acteam.vocago.data.model.ApiException
import com.acteam.vocago.data.model.ChangePasswordRequest
import com.acteam.vocago.data.model.CheckInResponse
import com.acteam.vocago.data.model.DeviceDTO
import com.acteam.vocago.data.model.GetTwoFAQrCodeResponse
import com.acteam.vocago.data.model.GetUserRankingResponse
import com.acteam.vocago.data.model.RankingResponse
import com.acteam.vocago.data.model.SetUp2FAResponse
import com.acteam.vocago.data.model.SuccessResponse
import com.acteam.vocago.data.model.UpdateUserDto
import com.acteam.vocago.data.model.UserDto
import com.acteam.vocago.domain.remote.UserRemoteDataSource
import com.acteam.vocago.utils.VocaGoRoutes
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
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

    override suspend fun changePassword(oldPassword: String, newPassword: String) {
        val response = client.post(VocaGoRoutes.ChangePassword.path) {
            contentType(ContentType.Application.Json)
            setBody(ChangePasswordRequest(oldPassword = oldPassword, newPassword = newPassword))
        }
        when (response.status) {
            HttpStatusCode.OK -> {
                return
            }

            else -> {
                throw ApiException(response.status.value)
            }
        }
    }

    override suspend fun getDevicesList(): Result<List<DeviceDTO>> {
        return try {
            val response = client.get(VocaGoRoutes.GetDevicesList.path)
            when (response.status) {
                HttpStatusCode.OK -> {
                    val data = response.body<SuccessResponse<List<DeviceDTO>>>()
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

    override suspend fun logoutUserDevice(credentials: List<String>): Result<Unit> {
        return try {
            val response = client.delete(VocaGoRoutes.LogoutUserDevice.path) {
                contentType(ContentType.Application.Json)
                setBody(mapOf("credentials" to credentials))
            }
            when (response.status) {
                HttpStatusCode.OK -> Result.success(Unit)
                else -> Result.failure(ApiException(response.status.value))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    override suspend fun getTwoFAQrCode(): GetTwoFAQrCodeResponse {
        val response = client.get(VocaGoRoutes.GetTwoFAQrCode.path)
        when (response.status) {
            HttpStatusCode.OK -> {
                val data = response.body<SuccessResponse<GetTwoFAQrCodeResponse>>()
                return data.data
            }

            else -> {
                throw ApiException(response.status.value)
            }
        }
    }

    override suspend fun setUp2FA(otpToken: String): SetUp2FAResponse {
        val response = client.post(VocaGoRoutes.SetUpTwoFA.path) {
            contentType(ContentType.Application.Json)
            setBody(mapOf("otpToken" to otpToken))
        }

        return when (response.status) {
            HttpStatusCode.OK -> response.body<SetUp2FAResponse>()
            else -> throw ApiException(response.status.value)
        }
    }


    override suspend fun disableTwoFA(): SetUp2FAResponse {
        val response = client.get(VocaGoRoutes.DisableTwoFA.path)
        return when (response.status) {
            HttpStatusCode.OK -> response.body<SetUp2FAResponse>()
            else -> throw ApiException(response.status.value)
        }
    }

    override suspend fun getUserRanking(): GetUserRankingResponse {
        val response = client.get(VocaGoRoutes.SelectCheckIn.path)
        return when (response.status) {
            HttpStatusCode.OK -> response.body<GetUserRankingResponse>()
            else -> throw ApiException(response.status.value)
        }
    }

    override suspend fun checkIn(): CheckInResponse {
        val response = client.post(VocaGoRoutes.CheckIn.path)
        return when (response.status) {
            HttpStatusCode.OK -> response.body<CheckInResponse>()
            else -> throw ApiException(response.status.value)
        }
    }

    override suspend fun getRanking(): RankingResponse {
        val response = client.get(VocaGoRoutes.GetRanking.path)
        return when (response.status) {
            HttpStatusCode.OK -> response.body<RankingResponse>()
            else -> throw ApiException(response.status.value)
        }
    }


}
