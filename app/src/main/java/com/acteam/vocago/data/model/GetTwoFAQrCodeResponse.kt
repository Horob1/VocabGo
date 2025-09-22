package com.acteam.vocago.data.model

import kotlinx.serialization.Serializable

@Serializable
data class GetTwoFAQrCodeResponse(
    val qrcode: String
)