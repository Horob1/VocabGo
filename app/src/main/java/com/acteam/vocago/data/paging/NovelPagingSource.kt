package com.acteam.vocago.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.acteam.vocago.domain.model.Novel
import com.acteam.vocago.domain.remote.NovelRemoteDataSource

class NovelPagingSource(
    private val novelRemoteDataSource: NovelRemoteDataSource,
    private val keySearch: String,
) : PagingSource<Int, Novel>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Novel> {
        return try {
            val page = params.key ?: 1

            val response = novelRemoteDataSource.getNovelList(page, keySearch)

            if (response.isSuccess && response.getOrNull() != null) {
                val novelList = response.getOrNull()!!.data.map {
                    it.toNovel()
                }
                val meta = response.getOrNull()!!.meta

                LoadResult.Page(
                    data = novelList,
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = if (meta.page >= meta.totalPages) null else page + 1
                )
            } else {
                LoadResult.Error(Exception("Error"))
            }


        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Novel>): Int? {
        return state.anchorPosition?.let { anchor ->
            state.closestPageToPosition(anchor)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchor)?.nextKey?.minus(1)
        }
    }
}
