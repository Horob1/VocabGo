package com.acteam.vocago.data.model

import kotlinx.serialization.Serializable

@Serializable
data class NovelDetailChapterList(
    val _id: String,
    val chapterNumber: Int,
    val chapterTitle: String,
    val createdAt: String,
)

@Serializable
data class NovelDetailDto(
    val _id: String,
    val fictionNumber: Int,
    val fictionTitle: String,
    val totalChapters: Int,
    val image: String,
    val author: String,
    val chapters: List<NovelDetailChapterList>,
)