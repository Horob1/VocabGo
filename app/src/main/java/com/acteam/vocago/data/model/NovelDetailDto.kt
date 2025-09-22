package com.acteam.vocago.data.model

import com.acteam.vocago.domain.model.Novel
import com.acteam.vocago.domain.model.NovelDetail
import com.acteam.vocago.domain.model.NovelDetailChapter
import kotlinx.serialization.Serializable

@Serializable
data class NovelDetailChapterDto(
    val _id: String,
    val chapterNumber: Int,
    val chapterTitle: String,
    val createdAt: String,
) {
    fun toNovelDetailChapter(): NovelDetailChapter {
        return NovelDetailChapter(
            _id = _id,
            chapterNumber = chapterNumber,
            chapterTitle = chapterTitle,
            createdAt = createdAt
        )
    }
}

@Serializable
data class NovelDetailDto(
    val _id: String,
    val fictionNumber: Int,
    val fictionTitle: String,
    val totalChapters: Int,
    val image: String,
    val author: String,
    val chapters: List<NovelDetailChapterDto>,
) {
    fun toNovelDetail(): NovelDetail {
        return NovelDetail(
            _id = _id,
            fictionNumber = fictionNumber,
            fictionTitle = fictionTitle,
            totalChapters = totalChapters,
            image = image,
            author = author,
            chapters = chapters.map { it.toNovelDetailChapter() }
        )
    }

    fun toNovel(): Novel {
        return Novel(
            _id = _id,
            fictionNumber = fictionNumber,
            fictionTitle = fictionTitle,
            totalChapters = totalChapters,
            image = image,
            author = author,
            isPublish = true,
            createdAt = "",
            updatedAt = ""
        )
    }
}