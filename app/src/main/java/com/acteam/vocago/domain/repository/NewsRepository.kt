package com.acteam.vocago.domain.repository

import androidx.paging.PagingData
import com.acteam.vocago.data.local.entity.NewsEntity
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    fun getNewsPagingFlow(
        categories: List<String>,
        keySearch: String,
        level: String,
    ): Flow<PagingData<NewsEntity>>
}