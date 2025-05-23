package com.acteam.vocago.data.model

import kotlinx.serialization.Serializable

@Serializable
data class RoleDto(
    val name: String,
)

@Serializable
data class UserDto(
    val _id: String,
    val email: String,
    val username: String,
    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
    val address: String,
    val dob: String,
    val avatar: String,
    val roles: List<RoleDto>,
    val status: String,
    val createdAt: String,
    val updatedAt: String,
)