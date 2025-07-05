package com.acteam.vocago.data.model

import com.acteam.vocago.domain.model.Novel
import kotlinx.serialization.Serializable


@Serializable
data class NovelDto(
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
}
