package com.acteam.vocago.domain.model

data class Novel(
    val _id: String,
    val fictionNumber: Int,
    val fictionTitle: String,
    val totalChapters: Int,
    val image: String,
    val author: String,
    val isPublish: Boolean,
    val createdAt: String,
    val updatedAt: String,
)

data class NovelDetailChapter(
    val _id: String,
    val chapterNumber: Int,
    val chapterTitle: String,
    val createdAt: String,
)

data class NovelDetail(
    val _id: String,
    val fictionNumber: Int,
    val fictionTitle: String,
    val totalChapters: Int,
    val image: String,
    val author: String,
    val chapters: List<NovelDetailChapter>,
)