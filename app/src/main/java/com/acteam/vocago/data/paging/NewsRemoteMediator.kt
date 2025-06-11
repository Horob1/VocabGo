package com.acteam.vocago.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.acteam.vocago.data.local.AppDatabase
import com.acteam.vocago.data.local.entity.NewsEntity
import com.acteam.vocago.data.local.mapping.toNewsEntity
import com.acteam.vocago.domain.remote.NewsRemoteDataSource

@OptIn(ExperimentalPagingApi::class)
class NewsRemoteMediator(
    private val newsRemoteDataSource: NewsRemoteDataSource,
    private val appDatabase: AppDatabase,
    private val categories: List<String>,
    private val keySearch: String,
    private val level: String,
) : RemoteMediator<Int, NewsEntity>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, NewsEntity>,
    ): MediatorResult {
        try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> return MediatorResult.Success(
                    endOfPaginationReached = true
                )

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
                page = loadKey,
                limit = state.config.pageSize,
                categories = categories,
                keySearch = keySearch,
                level = level
            )

            if (response.isSuccess) {
                val body = response.getOrNull()
                    ?: return MediatorResult.Error(NullPointerException("Response body is null"))

                val meta = body.meta
                val data = body.data

                val entities = data.map { dto -> toNewsEntity(dto, loadKey) }

                appDatabase.withTransaction {
                    if (loadType == LoadType.REFRESH) {
                        appDatabase.newsDao().clearAllNews()
                    }
                    appDatabase.newsDao().insertNews(entities)
                }

                return MediatorResult.Success(endOfPaginationReached = meta.page >= meta.totalPages)
            } else {
                return MediatorResult.Error(IllegalStateException("Response failed"))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return MediatorResult.Error(e)
        }
    }
}