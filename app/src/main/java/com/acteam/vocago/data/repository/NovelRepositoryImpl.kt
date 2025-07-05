package com.acteam.vocago.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.acteam.vocago.data.paging.NovelPagingSource
import com.acteam.vocago.data.paging.ReadNovelPagingSource
import com.acteam.vocago.domain.model.Novel
import com.acteam.vocago.domain.remote.NovelRemoteDataSource
import com.acteam.vocago.domain.repository.NovelRepository
import kotlinx.coroutines.flow.Flow

class NovelRepositoryImpl(
    private val novelRemoteDataSource: NovelRemoteDataSource,
) : NovelRepository {
    override fun getNovelPagingFlow(keySearch: String): Flow<PagingData<Novel>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { NovelPagingSource(novelRemoteDataSource, keySearch) }
        ).flow
    }

    override fun getReadNovelPagingFlow(): Flow<PagingData<Novel>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { ReadNovelPagingSource(novelRemoteDataSource) }
        ).flow
    }
}