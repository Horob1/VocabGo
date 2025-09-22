package com.acteam.vocago.domain.repository

import androidx.paging.PagingData
import com.acteam.vocago.data.model.ChapterDto
import com.acteam.vocago.domain.model.Novel
import com.acteam.vocago.domain.model.NovelDetail
import kotlinx.coroutines.flow.Flow

interface NovelRepository {
    fun getNovelPagingFlow(keySearch: String): Flow<PagingData<Novel>>
    fun getReadNovelPagingFlow(): Flow<PagingData<Novel>>

    // Get NovelDetail
    suspend fun getNovelDetail(id: String): Result<NovelDetail>

    suspend fun saveNovel(novel: NovelDetail)

    suspend fun getReadNovelFirstPage(): List<Novel>

    fun getReadNovelFlow(): Flow<List<Novel>>

    suspend fun getChapterDetail(chapterId: String): Result<ChapterDto>
}