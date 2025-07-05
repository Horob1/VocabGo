package com.acteam.vocago.domain.usecase

import com.acteam.vocago.domain.remote.NovelRemoteDataSource

class GetChapterUseCase(
    private val novelRemoteDataSource: NovelRemoteDataSource,
) {
    suspend operator fun invoke(chapterId: String) =
        novelRemoteDataSource.getChapterDetail(chapterId).getOrNull()
}