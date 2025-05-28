package com.acteam.vocago.data.model

import com.acteam.vocago.data.local.entity.NewsEntity
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
) {
    fun toNewsEntity(page: Int): NewsEntity {
        return NewsEntity(
            _id = _id,
            category = category,
            title = title,
            url = url,
            coverImage = coverImage,
            content = content,
            views = views,
            level = level,
            ratingCount = ratingCount,
            ratingSum = ratingSum,
            ratingAvg = ratingAvg,
            words = words,
            isPublished = isPublished,
            questions = questions,
            tags = tags,
            translations = translations,
            createdAt = createdAt,
            page = page,
        )
    }
}