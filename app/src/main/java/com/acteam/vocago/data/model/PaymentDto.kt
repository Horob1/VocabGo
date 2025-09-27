package com.acteam.vocago.data.model

import kotlinx.serialization.Serializable

@Serializable
data class PaymentRequest(
    val amount: Int,
    val orderInfo: String,
    val extraData: String
)

@Serializable
data class PaymentResponse(
    val payUrl: String
)
