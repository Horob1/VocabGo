package com.acteam.vocago.data.model

import kotlinx.serialization.Serializable

@Serializable
data class RankingResponse(
    val success: Boolean,
    val message: String,
    val data: RankingData
)

@Serializable
data class RankingData(
    val top8: List<RankingEntry>,
    val self: RankingEntry
)

@Serializable
data class RankingEntry(
    val _id: String,
    val userId: RankingUser,
    val currentStreak: Int,
    val maxStreak: Int,
    val lastCheckInDate: String,
    val maxToeicPoint: Int,
    val totalPoints: Int,
    val createdAt: String,
    val updatedAt: String
)

@Serializable
data class RankingUser(
    val _id: String,
    val username: String
)
