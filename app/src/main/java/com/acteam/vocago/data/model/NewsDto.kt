package com.acteam.vocago.data.model

import kotlinx.serialization.Serializable

@Serializable
data class WordNewsDto(
    val word: String,
    val level: String,
)

@Serializable
data class QuestionNewsDto(
    val question: String,
    val answers: List<String>,
    val correctAnswerIndex: Int,
    val explanation: String,
)

@Serializable
data class TranslationNewsDto(
    val targetLanguage: String,
    val translation: String,
)

@Serializable
data class NewsDto(
    val _id: String,
    val category: String,
    val title: String,
    val url: String,
    val coverImage: String,
    val views: Int,
    val level: String,
    val words: List<WordNewsDto>,
    val isPublished: Boolean,
    val createdAt: String,
)

@Serializable
data class NewsDetailLogQsaDto(
    val qsIndex: Int,
    val chosenAnswer: Int,
)

@Serializable
data class NewLogDto(
    val _id: String,
    val newsId: String,
    val userId: String,
    val rating: Int,
    val score: Int,
    val isBookmarked: Boolean,
    val questionLogs: List<NewsDetailLogQsaDto>,
    val updatedAt: String,
    val createdAt: String,
)

@Serializable
data class NewsDetailDto(
    val _id: String,
    val category: String,
    val title: String,
    val url: String,
    val coverImage: String,
    val content: String,
    val views: Int,
    val level: String,
    val ratingCount: Int,
    val ratingSum: Int,
    val ratingAvg: Double,
    val words: List<WordNewsDto>,
    val isPublished: Boolean,
    val questions: List<QuestionNewsDto>,
    val tags: List<String>,
    val translations: List<TranslationNewsDto>,
    val createdAt: String,
    val log: NewLogDto?,
    val related: List<NewsDto>,
    val bookmarkCount: Int,
)