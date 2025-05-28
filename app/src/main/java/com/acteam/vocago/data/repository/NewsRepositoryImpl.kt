package com.acteam.vocago.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.acteam.vocago.data.local.dao.NewsDao
import com.acteam.vocago.data.local.entity.NewsEntity
import com.acteam.vocago.data.paging.NewsRemoteMediator
import com.acteam.vocago.domain.remote.NewsRemoteDataSource
import com.acteam.vocago.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow

class NewsRepositoryImpl(
    private val newsRemoteDataSource: NewsRemoteDataSource,
    private val newsDao: NewsDao,
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
                enablePlaceholders = false
            ),
            remoteMediator = NewsRemoteMediator(
                newsRemoteDataSource = newsRemoteDataSource,
                newsDao = newsDao,
                categories = categories,
                keySearch = keySearch,
                level = level
            ),
            pagingSourceFactory = {
                newsDao.getAllNewsInInsertOrder()
            }
        ).flow
    }
}