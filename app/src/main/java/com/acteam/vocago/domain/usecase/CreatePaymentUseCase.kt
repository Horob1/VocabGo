package com.acteam.vocago.domain.usecase

import com.acteam.vocago.data.model.PaymentRequest
import com.acteam.vocago.data.model.PaymentResponse
import com.acteam.vocago.domain.remote.PaymentRemoteDataSource

class CreatePaymentUseCase(
    private val paymentRemoteDataSource: PaymentRemoteDataSource
) {
    suspend operator fun invoke(request: PaymentRequest): PaymentResponse {
        return paymentRemoteDataSource.createPayment(request)
    }
}
