package com.acteam.vocago.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ResetPasswordRequest (
    val email: String,
    val otp: String,
    val password: String
)