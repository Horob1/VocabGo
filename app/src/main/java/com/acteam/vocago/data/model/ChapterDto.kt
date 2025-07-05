package com.acteam.vocago.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ChapterPayload(
    val _id: String,
    val fictionId: String,
    val chapterNumber: Int,
    val chapterTitle: String,
    val content: String,
    val createdAt: String,
)

@Serializable
data class NearChapterPayload(
    val _id: String,
    val chapterNumber: Int,
    val chapterTitle: String,
)

@Serializable
data class ChapterDto(
    val chapter: ChapterPayload,
    val previousChapter: NearChapterPayload?,
    val nextChapter: NearChapterPayload?,
)
