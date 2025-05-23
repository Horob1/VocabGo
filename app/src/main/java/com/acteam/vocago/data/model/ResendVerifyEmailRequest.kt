package com.acteam.vocago.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ResendVerifyEmailRequest(
    val email: String
)