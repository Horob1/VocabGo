package com.acteam.vocago.network

import com.acteam.vocago.BuildConfig
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

val vocabGoBeNetwork = HttpClient(OkHttp) {
    defaultRequest {
        url(BuildConfig.BASE_URL)
    }
    install(ContentNegotiation) {
        json(Json { ignoreUnknownKeys = true })
    }
    install(Logging) {
        level = LogLevel.ALL
    }
}

const val VOCAB_GO_BE_QUALIFIER = "VOCAB_GO_BE_QUALIFIER"