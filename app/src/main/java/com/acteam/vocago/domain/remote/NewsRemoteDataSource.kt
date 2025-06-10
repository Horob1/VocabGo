package com.acteam.vocago.domain.remote

import com.acteam.vocago.data.model.NewsDetailDto
import com.acteam.vocago.data.model.NewsDetailLogQsaDto
import com.acteam.vocago.data.model.NewsDto
import com.acteam.vocago.data.model.NewsHistoryDto
import com.acteam.vocago.data.model.PaginatedResponse


interface NewsRemoteDataSource {
    suspend fun getNews(
        page: Int,
        limit: Int,
        categories: List<String>,
        keySearch: String,
        level: String,
    ): Result<PaginatedResponse<NewsDto>>

    suspend fun getNewsHistories(
        page: Int,
        limit: Int,
    ): Result<PaginatedResponse<NewsHistoryDto>>

    suspend fun getNewsDetail(id: String): Result<NewsDetailDto>

    suspend fun submitPractice(
        newsId: String,
        score: Int,
        questionLogs: List<NewsDetailLogQsaDto>,
    ): Result<Unit>

    suspend fun toggleBookmark(newsId: String, isBookmarked: Boolean): Result<Unit>
}