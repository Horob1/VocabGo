package com.acteam.vocago.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NewsHistoryItemDto(
    val _id: String,
    val category: String,
    val title: String,
    val url: String,
    val coverImage: String,
    val views: Int,
    val level: String,
    val createdAt: String,
)

@Serializable
data class NewsHistoryDto(
    val _id: String,
    @SerialName("newsId")
    val news: NewsHistoryItemDto,
    val score: Int,
    val isBookmarked: Boolean,
    val updatedAt: String,
    val createdAt: String,
)