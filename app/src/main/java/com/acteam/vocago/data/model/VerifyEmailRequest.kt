package com.acteam.vocago.data.model

import kotlinx.serialization.Serializable

@Serializable
data class VerifyEmailRequest(
    val email: String,
    val otp: String
)