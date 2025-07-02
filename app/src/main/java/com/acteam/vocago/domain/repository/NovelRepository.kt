package com.acteam.vocago.domain.repository

import androidx.paging.PagingData
import com.acteam.vocago.domain.model.Novel
import kotlinx.coroutines.flow.Flow

interface NovelRepository {
    fun getNovelPagingFlow(keySearch: String): Flow<PagingData<Novel>>
}