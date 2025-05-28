package com.acteam.vocago.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.acteam.vocago.data.model.QuestionNewsDto
import com.acteam.vocago.data.model.TranslationNewsDto
import com.acteam.vocago.data.model.WordNewsDto

@Entity(tableName = "news")
data class NewsEntity(
    // auto id để dễ get nhé anh Công, nó sẽ tự tăng theo thứ tự tăng dần
    @PrimaryKey(autoGenerate = true)
    val autoId: Long = 0L,
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
    // Cái này dùng để dùng với paging nhé
    val page: Int,
)