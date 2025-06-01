package com.acteam.vocago.domain.remote

import com.acteam.vocago.data.model.NewsDetailDto
import com.acteam.vocago.data.model.NewsDto
import com.acteam.vocago.data.model.PaginatedResponse


interface NewsRemoteDataSource {
    suspend fun getNews(
        page: Int,
        limit: Int,
        categories: List<String>,
        keySearch: String,
        level: String,
    ): Result<PaginatedResponse<NewsDto>>

    suspend fun getNewsDetail(id: String): Result<NewsDetailDto>
}