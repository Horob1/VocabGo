package com.acteam.vocago.domain.usecase

import com.acteam.vocago.data.model.NovelDetailDto
import com.acteam.vocago.domain.remote.NovelRemoteDataSource

class GetNovelDetailUseCase(
    private val novelRemoteDataSource: NovelRemoteDataSource,
) {
    suspend operator fun invoke(id: String): NovelDetailDto? {
        return novelRemoteDataSource.getNovelDetail(id).getOrNull()
    }

}