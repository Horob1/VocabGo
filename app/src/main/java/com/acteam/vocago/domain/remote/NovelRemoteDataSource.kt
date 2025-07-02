package com.acteam.vocago.domain.remote

import com.acteam.vocago.data.model.NovelDetailDto
import com.acteam.vocago.data.model.NovelDto
import com.acteam.vocago.data.model.PaginatedResponse
import com.acteam.vocago.domain.model.Novel

interface NovelRemoteDataSource {
    suspend fun getFirstPageOnline(): Result<List<Novel>>

    suspend fun getNovelList(page: Int, keySearch: String): Result<PaginatedResponse<NovelDto>>

    suspend fun getNovelDetail(id: String): Result<NovelDetailDto>
}