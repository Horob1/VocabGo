package com.acteam.vocago.data.model

import kotlinx.serialization.Serializable


@Serializable
data class SetUp2FAResponse(
    val success: Boolean,
    val message: String,
    val data: UserData
)

@Serializable
data class UserData(
    val _id: String,
    val username: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
    val address: String,
    val dob: String,
    val gender: String,
    val emailVerified: Boolean,
    val roles: List<Role>,
    val avatar: String,
    val status: String,
    val require2FA: Boolean,
    val createdAt: String,
    val updatedAt: String
)

@Serializable
data class Role(
    val name: String,
)