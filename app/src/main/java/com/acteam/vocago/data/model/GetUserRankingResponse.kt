package com.acteam.vocago.data.model

import  kotlinx.serialization.Serializable

@Serializable
data class GetUserRankingResponse(
    val success: Boolean,
    val message: String,
    val data: UserRankingData
)

@Serializable
data class UserRankingData(
    val _id: String,
    val userId: String,
    val currentStreak: Int,
    val maxStreak: Int,
    val lastCheckInDate: String,
    val maxToeicPoint: Int,
    val totalPoints: Int,
    val createdAt: String,
    val updatedAt: String,
    val isCheckedInToday: Boolean
)