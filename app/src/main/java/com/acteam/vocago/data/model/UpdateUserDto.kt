package com.acteam.vocago.data.model

import kotlinx.serialization.Serializable

@Serializable
data class UpdateUserDto(
    val firstName: String,
    val lastName: String,
    val address: String,
    val dob: String,
)