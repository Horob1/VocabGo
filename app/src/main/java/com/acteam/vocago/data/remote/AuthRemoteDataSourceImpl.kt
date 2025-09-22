package com.acteam.vocago.data.remote

import com.acteam.vocago.data.model.ApiException
import com.acteam.vocago.data.model.ForgotPasswordRequest
import com.acteam.vocago.data.model.LoginGoogleRequest
import com.acteam.vocago.data.model.LoginGoogleResponse
import com.acteam.vocago.data.model.LoginRequest
import com.acteam.vocago.data.model.LoginResponse
import com.acteam.vocago.data.model.LogoutRequest
import com.acteam.vocago.data.model.RegisterRequest
import com.acteam.vocago.data.model.ResendVerifyEmailRequest
import com.acteam.vocago.data.model.ResetPasswordRequest
import com.acteam.vocago.data.model.SuccessResponse
import com.acteam.vocago.data.model.VerifyEmailRequest
import com.acteam.vocago.data.model.VerifyTwoFARequest
import com.acteam.vocago.data.model.VerifyTwoFAResponse
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
    private val client: HttpClient,
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

    override suspend fun register(
        username: String,
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        phoneNumber: String,
        address: String,
        dob: String,
        gender: String,
    ) {
        val response = client.post(VocaGoRoutes.Register.path) {
            contentType(ContentType.Application.Json)
            setBody(
                RegisterRequest(
                    firstName = firstName,
                    lastName = lastName,
                    username = username,
                    email = email,
                    password = password,
                    phoneNumber = phoneNumber,
                    dob = dob,
                    gender = gender,
                    address = address
                )
            )
        }
        when (response.status) {
            HttpStatusCode.Created -> {
                return
            }

            else -> {
                throw ApiException(response.status.value)
            }
        }
    }

    override suspend fun verifyEmail(email: String, otp: String) {
        val response = client.post(VocaGoRoutes.VerifyEmail.path) {
            contentType(ContentType.Application.Json)
            setBody(VerifyEmailRequest(email = email, otp = otp))
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

    override suspend fun resendVerifyEmail(email: String) {
        val response = client.post(VocaGoRoutes.ResendVerifyEmail.path) {
            contentType(ContentType.Application.Json)
            setBody(ResendVerifyEmailRequest(email = email))
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

    override suspend fun verifyTwoFA(email: String, otpToken: String): VerifyTwoFAResponse {
        val response = client.post(VocaGoRoutes.VerifyTwoFA.path) {
            contentType(ContentType.Application.Json)
            setBody(VerifyTwoFARequest(email = email, otpToken = otpToken))
        }
        when (response.status) {
            HttpStatusCode.OK -> {
                val data = response.body<SuccessResponse<VerifyTwoFAResponse>>()
                return data.data
            }

            else -> {
                throw ApiException(response.status.value)
            }
        }
    }

    override suspend fun loginGoogle(token: String): LoginGoogleResponse {
        val response = client.post(VocaGoRoutes.LoginGoogle.path) {
            contentType(ContentType.Application.Json)
            setBody(LoginGoogleRequest(token = token))
        }
        when (response.status) {
            HttpStatusCode.OK -> {
                val data = response.body<SuccessResponse<LoginGoogleResponse>>()
                return data.data
            }

            else -> {
                throw ApiException(response.status.value)
            }
        }
    }

    override suspend fun logout(credentialId: String) {
        val response = client.post(VocaGoRoutes.Logout.path) {
            contentType(ContentType.Application.Json)
            setBody(LogoutRequest(credentialId))
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