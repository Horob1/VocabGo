package com.acteam.vocago.di

import android.util.Log
import com.acteam.vocago.BuildConfig
import com.acteam.vocago.domain.local.AuthEncryptedPreferences
import com.acteam.vocago.utils.RefreshTokenAuthenticator
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.koin.core.qualifier.named
import org.koin.dsl.module

val networkModule = module {
    single(named(VOCAB_GO_BE_QUALIFIER)) {
        val authPreferences: AuthEncryptedPreferences =
            get()
        val client = OkHttpClient.Builder()
            .addInterceptor(Interceptor { chain ->
                val accessToken =
                    authPreferences.getAccessToken() // Get the access token from the preferences
                val request = chain.request().newBuilder().apply {
                    accessToken?.let {
                        header(
                            "Authorization",
                            "Bearer $it"
                        )
                    }
                }.build()
                chain.proceed(request)
            })
            .authenticator(RefreshTokenAuthenticator(authPreferences))
            .build()

        HttpClient(OkHttp) {
            engine {
                preconfigured = client
            }
            defaultRequest {
                url(BuildConfig.BASE_URL)
                header("X-Platform", "MOBILE")
            }
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
            install(Logging) {
                level = LogLevel.ALL
            }
        }
    }
}

const val VOCAB_GO_BE_QUALIFIER = "VOCAB_GO_BE_QUALIFIER"