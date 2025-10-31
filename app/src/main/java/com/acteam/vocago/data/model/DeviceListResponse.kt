package com.acteam.vocago.data.model

import kotlinx.serialization.Serializable

@Serializable
data class DeviceDTO(
    val _id: String,
    val userId: String,
    val ip: String,
    val userAgent: String,
    val iat: String,
    val exp: String,
    val fcmTokens:  List<String>? = null ,
    val is2FAVerified: Boolean = false,
    val createdAt: String,
    val updatedAt: String,
    val isCurrent: Boolean = false
)
