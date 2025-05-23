package com.acteam.vocago.data.model

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String,

    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
    val address: String,
    val dob: String,
    val gender: String,
)