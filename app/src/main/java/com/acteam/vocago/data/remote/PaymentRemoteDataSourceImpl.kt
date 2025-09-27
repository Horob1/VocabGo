package com.acteam.vocago.data.remote

import com.acteam.vocago.data.model.ApiException
import com.acteam.vocago.data.model.PaymentRequest
import com.acteam.vocago.data.model.PaymentResponse
import com.acteam.vocago.domain.remote.PaymentRemoteDataSource
import com.acteam.vocago.utils.VocaGoRoutes
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType

class PaymentRemoteDataSourceImpl(
    private val client: HttpClient,
) : PaymentRemoteDataSource {

    override suspend fun createPayment(request: PaymentRequest): PaymentResponse {
        val response = client.post(VocaGoRoutes.CreatePayment.path) {
            contentType(ContentType.Application.Json)
            setBody(request)
        }

        return when (response.status) {
            HttpStatusCode.OK, HttpStatusCode.Created -> {
                response.body<PaymentResponse>()
            }

            else -> {
                throw ApiException(response.status.value)
            }
        }
    }
}
