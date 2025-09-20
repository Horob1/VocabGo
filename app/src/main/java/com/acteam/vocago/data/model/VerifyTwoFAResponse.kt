package com.acteam.vocago.data.model

import kotlinx.serialization.Serializable

@Serializable
data class VerifyTwoFAResponse(
    val accessToken: String,
    val refreshToken: String,
    val credentialId: String
)