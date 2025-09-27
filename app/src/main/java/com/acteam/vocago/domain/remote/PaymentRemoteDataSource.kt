package com.acteam.vocago.domain.remote

import com.acteam.vocago.data.model.PaymentRequest
import com.acteam.vocago.data.model.PaymentResponse

interface PaymentRemoteDataSource {
    suspend fun createPayment(request: PaymentRequest): PaymentResponse
}