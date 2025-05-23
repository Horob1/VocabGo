package com.acteam.vocago.data.model

import kotlinx.serialization.Serializable

@Serializable
data class VerifyTwoFARequest(
    val email: String,
    val otpToken: String
)