package com.acteam.vocago.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.acteam.vocago.data.local.AppDatabase
import com.acteam.vocago.data.local.entity.NewsHistoryEntity
import com.acteam.vocago.data.local.mapping.toNewsHistoryEntity
import com.acteam.vocago.domain.remote.NewsRemoteDataSource

@OptIn(ExperimentalPagingApi::class)
class NewsLogRemoteMediator(
    private val newsRemoteDataSource: NewsRemoteDataSource,
    private val appDatabase: AppDatabase,
) : RemoteMediator<Int, NewsHistoryEntity>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, NewsHistoryEntity>,
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
            val response = newsRemoteDataSource.getNewsHistories(
                page = loadKey,
                limit = state.config.pageSize,
            )

            if (response.isSuccess) {
                val body = response.getOrNull()
                    ?: return MediatorResult.Error(NullPointerException("Response body is null"))

                val meta = body.meta
                val data = body.data

                val entities = data.map { dto -> toNewsHistoryEntity(dto, loadKey) }

                appDatabase.withTransaction {
                    if (loadType == LoadType.REFRESH) {
                        appDatabase.newsHistoryDao().clearAllNews()
                    }
                    appDatabase.newsHistoryDao().insertNews(entities)
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