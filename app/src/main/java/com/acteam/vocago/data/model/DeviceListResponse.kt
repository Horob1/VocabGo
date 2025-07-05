package com.acteam.vocago.data.model

import kotlinx.serialization.Serializable

data class DeviceListResponse(
    val success: Boolean,
    val message: String,
    val data: List<DeviceDTO>
)

@Serializable
data class DeviceDTO(
    val _id: String,
    val userId: String,
    val ip: String,
    val userAgent: String,
    val iat: String,
    val exp: String,
    val fcmTokens: String?,
    val is2FAVerified: Boolean,
    val createdAt: String,
    val updatedAt: String
)