package com.acteam.vocago.data.remote

import android.util.Log
import com.acteam.vocago.data.model.LoginRequest
import com.acteam.vocago.domain.remote.AuthRemoteDataSource
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType


class AuthRemoteDataSourceImpl(
    val client: HttpClient,
) : AuthRemoteDataSource {
    override suspend fun login(username: String, password: String) {
        try {
            val response = client.post("/api/v1/user/login") {
                contentType(ContentType.Application.Json)
                setBody(LoginRequest(usernameOrEmail = username, password = password))
            }

            Log.d("AuthRemoteDataSourceImpl", response.toString())
        } catch (e: Exception) {
            Log.e("AuthRemoteDataSourceImpl", e.toString())
        }
    }
}