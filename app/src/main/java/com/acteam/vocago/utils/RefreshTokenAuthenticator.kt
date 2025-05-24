package com.acteam.vocago.utils

import android.util.Log
import com.acteam.vocago.BuildConfig
import com.acteam.vocago.data.model.RefreshTokenRequest
import com.acteam.vocago.data.model.RefreshTokenResponse
import com.acteam.vocago.domain.local.AuthEncryptedPreferences
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class RefreshTokenAuthenticator(
    private val authPreferences: AuthEncryptedPreferences,
) : Authenticator {

    private val refreshClient = HttpClient {
        defaultRequest {
            header("X-Platform", "MOBILE")
        }
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
        install(HttpTimeout) {
            connectTimeoutMillis = 10_000
            requestTimeoutMillis = 15_000
        }
    }

    private val refreshMutex = Mutex()
    private var refreshingJob: Deferred<RefreshTokenResponse?>? = null

    override fun authenticate(route: Route?, response: Response): Request? {
        if (responseCount(response) >= 2) return null

        return runBlocking {
            refreshMutex.withLock {
                if (refreshingJob == null) {
                    refreshingJob = CoroutineScope(Dispatchers.IO).async {
                        refreshToken()
                    }
                }
            }
            val tokenResponse = try {
                refreshingJob?.await()
            } finally {
                refreshMutex.withLock {
                    refreshingJob = null
                }
            }

            tokenResponse?.let {
                authPreferences.refreshToken(it.refreshToken, it.accessToken)
                response.request.newBuilder()
                    .header("Authorization", "Bearer ${it.accessToken}")
                    .build()
            }

        }
    }

    private suspend fun refreshToken(): RefreshTokenResponse? {
        val refreshToken = authPreferences.getRefreshToken()
        if (refreshToken.isNullOrBlank()) {
            authPreferences.clearCredentials()
            return null
        }

        return try {
            val response = refreshClient.post(BuildConfig.BASE_URL + "/" + VocaGoRoutes.RefreshToken.path) {
                contentType(ContentType.Application.Json)
                setBody(RefreshTokenRequest(token = refreshToken))
            }
            val responseBodyString = response.bodyAsText()
            Log.d("Authenticator", "RefreshToken response body: $responseBodyString")

            if (response.status == HttpStatusCode.OK) {
                val json = Json { ignoreUnknownKeys = true }
                val jsonObject = json.parseToJsonElement(responseBodyString).jsonObject
                val dataElement = jsonObject["data"] ?: return null

                json.decodeFromJsonElement(
                    RefreshTokenResponse.serializer(),
                    dataElement
                )
            } else if (response.status == HttpStatusCode.Unauthorized) {
                authPreferences.clearCredentials()
                null
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("Authenticator", "Refresh token failed", e)
            null
        }
    }

    private fun responseCount(response: Response): Int {
        var count = 1
        var res = response
        while (res.priorResponse != null) {
            count++
            res = res.priorResponse!!
        }
        return count
    }

}
