package com.acteam.vocago.domain.repository

import androidx.paging.PagingData
import com.acteam.vocago.data.local.entity.NewsEntity
import com.acteam.vocago.data.local.entity.NewsHistoryEntity
import com.acteam.vocago.data.model.NewsDetailDto
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    fun getNewsPagingFlow(
        categories: List<String>,
        keySearch: String,
        level: String,
    ): Flow<PagingData<NewsEntity>>

    fun getNewsHistoriesPagingFlow(): Flow<PagingData<NewsHistoryEntity>>

    suspend fun getNewsDetail(id: String): NewsDetailDto?
}