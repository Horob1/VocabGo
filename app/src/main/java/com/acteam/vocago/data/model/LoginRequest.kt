package com.acteam.vocago.data.model

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(val usernameOrEmail: String, val password: String)