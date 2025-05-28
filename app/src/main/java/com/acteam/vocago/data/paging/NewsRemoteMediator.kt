package com.acteam.vocago.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.acteam.vocago.data.local.dao.NewsDao
import com.acteam.vocago.data.local.entity.NewsEntity
import com.acteam.vocago.domain.remote.NewsRemoteDataSource

@OptIn(ExperimentalPagingApi::class)
class NewsRemoteMediator(
    private val newsRemoteDataSource: NewsRemoteDataSource,
    private val newsDao: NewsDao,
    private val categories: List<String>,
    private val keySearch: String,
    private val level: String,
) : RemoteMediator<Int, NewsEntity>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, NewsEntity>,
    ): MediatorResult {

        try {
            val page = when (loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                    if (lastItem == null) {
                        1
                    } else {
                        lastItem.page + 1
                    }
                }
            }

            val response = newsRemoteDataSource.getNews(
                page = page,
                limit = state.config.pageSize,
                categories = categories,
                keySearch = keySearch,
                level = level
            )

            if (response.isSuccess) {
                response.getOrNull()?.let { body ->
                    val meta = body.meta
                    val data = body.data

                    val entities = data.map { dto ->
                        dto.toNewsEntity(page = meta.page)
                    }

                    if (loadType == LoadType.REFRESH) {
                        newsDao.clearAllNews()
                    }

                    newsDao.insertNews(entities)

                    return MediatorResult.Success(endOfPaginationReached = meta.page >= meta.totalPages)
                }
            }

            return MediatorResult.Error(IllegalStateException("Response failed"))
        } catch (e: Exception) {
            e.printStackTrace()
            return MediatorResult.Error(e)

        }
    }
}