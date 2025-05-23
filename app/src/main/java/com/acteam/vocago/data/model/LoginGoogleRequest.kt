package com.acteam.vocago.data.model

import kotlinx.serialization.Serializable

@Serializable
data class LoginGoogleRequest(
    val token: String
)