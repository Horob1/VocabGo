package com.acteam.vocago.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.acteam.vocago.data.model.NovelDto
import com.acteam.vocago.domain.model.Novel

@Entity(tableName = "novels")
data class NovelEntity(
    @PrimaryKey
    val _id: String,
    val fictionNumber: Int,
    val fictionTitle: String,
    val totalChapters: Int,
    val image: String,
    val author: String,
    val isPublish: Boolean,
    val createdAt: String,
    val updatedAt: String,
) {
    fun toNovel(): Novel = Novel(
        _id = _id,
        fictionNumber = fictionNumber,
        fictionTitle = fictionTitle,
        totalChapters = totalChapters,
        image = image,
        author = author,
        isPublish = isPublish,
        createdAt = createdAt,
        updatedAt = updatedAt
    )

    fun toNovelDto(): NovelDto = NovelDto(
        _id = _id,
        fictionNumber = fictionNumber,
        fictionTitle = fictionTitle,
        totalChapters = totalChapters,
        image = image,
        author = author,
        isPublish = isPublish,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}