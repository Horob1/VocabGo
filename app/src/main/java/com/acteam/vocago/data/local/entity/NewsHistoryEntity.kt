package com.acteam.vocago.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.acteam.vocago.data.model.NewsHistoryItemDto

@Entity(tableName = "news_history")
data class NewsHistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val autoId: Long = 0L,
    val _id: String,
    val isBookmarked: Boolean,
    val createdAt: String,
    val updatedAt: String,
    val score: Int,
    val news: NewsHistoryItemDto,
    val page: Int,
)