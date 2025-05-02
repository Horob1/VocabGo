package com.acteam.vocago.data.model

import kotlinx.serialization.Serializable

@Serializable
data class PaginatedResponse<T>(
    val success: Boolean,
    val meta: MetaResponse,
    val data: List<T>,
    val message: String
)