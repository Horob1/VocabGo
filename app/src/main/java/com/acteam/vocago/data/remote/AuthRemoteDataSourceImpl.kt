package com.acteam.vocago.data.remote

import com.acteam.vocago.data.model.ApiException
import com.acteam.vocago.data.model.ForgotPasswordRequest
import com.acteam.vocago.data.model.LoginRequest
import com.acteam.vocago.data.model.LoginResponse
import com.acteam.vocago.data.model.ResetPasswordRequest
import com.acteam.vocago.data.model.SuccessResponse
import com.acteam.vocago.domain.remote.AuthRemoteDataSource
import com.acteam.vocago.utils.VocaGoRoutes
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType


class AuthRemoteDataSourceImpl(
    val client: HttpClient,
) : AuthRemoteDataSource {
    override suspend fun login(username: String, password: String): LoginResponse {
        val response = client.post(VocaGoRoutes.Login.path) {
            contentType(ContentType.Application.Json)
            setBody(LoginRequest(usernameOrEmail = username, password = password))
        }
        when (response.status) {
            HttpStatusCode.OK -> {
                val data = response.body<SuccessResponse<LoginResponse>>()
                return data.data
            }

            else -> {
                throw ApiException(response.status.value)
            }
        }
    }
    override suspend fun forgotPassword(email: String) {
        val response = client.post(VocaGoRoutes.ForgotPassword.path) {
            contentType(ContentType.Application.Json)
            setBody(ForgotPasswordRequest(email = email))
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
    override suspend fun resetPassword(email: String, otp: String, password: String) {
        val response = client.post(VocaGoRoutes.ResetPassword.path) {
            contentType(ContentType.Application.Json)
            setBody(ResetPasswordRequest(email = email, otp = otp, password = password))
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
}