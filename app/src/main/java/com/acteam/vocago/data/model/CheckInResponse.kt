package com.acteam.vocago.data.model


import kotlinx.serialization.Serializable

@Serializable
data class CheckInResponse(
    val success: Boolean,
    val message: String,
    val data: UserRankingData
)
