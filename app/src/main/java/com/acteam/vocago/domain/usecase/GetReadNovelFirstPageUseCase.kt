package com.acteam.vocago.domain.usecase

import com.acteam.vocago.domain.remote.NovelRemoteDataSource

class GetReadNovelFirstPageUseCase(
    private val novelRemoteDataSource: NovelRemoteDataSource,
) {
    suspend operator fun invoke() = novelRemoteDataSource.getReadNovelFirstPage()


}