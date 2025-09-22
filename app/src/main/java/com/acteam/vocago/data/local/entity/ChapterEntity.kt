package com.acteam.vocago.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chapters")
data class ChapterEntity(
    @PrimaryKey
    val _id: String,
    val fictionId: String,
    val chapterNumber: Int,
    val chapterTitle: String,
    val content: String,
    val createdAt: String,
    val nextChapterId: String?,
    val previousChapterId: String?,
)
