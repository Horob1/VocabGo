package com.acteam.vocago.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.acteam.vocago.data.local.AppDatabase
import com.acteam.vocago.data.local.entity.NewsEntity
import com.acteam.vocago.data.local.entity.NewsHistoryEntity
import com.acteam.vocago.data.model.NewsDetailDto
import com.acteam.vocago.data.paging.NewsLogRemoteMediator
import com.acteam.vocago.data.paging.NewsRemoteMediator
import com.acteam.vocago.domain.remote.NewsRemoteDataSource
import com.acteam.vocago.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow

class NewsRepositoryImpl(
    private val newsRemoteDataSource: NewsRemoteDataSource,
    private val appDatabase: AppDatabase,
) : NewsRepository {
    @OptIn(ExperimentalPagingApi::class)
    override fun getNewsPagingFlow(
        categories: List<String>,
        keySearch: String,
        level: String,
    ): Flow<PagingData<NewsEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                initialLoadSize = 10,
                prefetchDistance = 5,
                maxSize = 200,
                enablePlaceholders = false
            ),
            remoteMediator = NewsRemoteMediator(
                newsRemoteDataSource = newsRemoteDataSource,
                categories = categories,
                keySearch = keySearch,
                level = level,
                appDatabase = appDatabase
            ),
            pagingSourceFactory = {
                appDatabase.newsDao().getAllNewsInInsertOrder()
            }
        ).flow
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getNewsHistoriesPagingFlow(): Flow<PagingData<NewsHistoryEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                initialLoadSize = 10,
                prefetchDistance = 5,
                maxSize = 200,
                enablePlaceholders = false
            ),
            remoteMediator = NewsLogRemoteMediator(
                newsRemoteDataSource = newsRemoteDataSource,
                appDatabase = appDatabase
            ),
            pagingSourceFactory = {
                appDatabase.newsHistoryDao().getAllNewsHistoryInInsertOrder()
            }
        ).flow
    }

    override suspend fun getNewsDetail(id: String): NewsDetailDto? {
        val response = newsRemoteDataSource.getNewsDetail(id)
        if (response.isSuccess) {
            return response.getOrNull()!!
        }

        return null
    }
}