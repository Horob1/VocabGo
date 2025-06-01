package com.acteam.vocago.data.model

import kotlinx.serialization.Serializable

@Serializable
data class LoginGoogleResponse(
    val credentialId: String,
    val accessToken: String,
    val refreshToken: String,
)