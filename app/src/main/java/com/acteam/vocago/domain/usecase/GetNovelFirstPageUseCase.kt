package com.acteam.vocago.domain.usecase

import com.acteam.vocago.domain.model.Novel
import com.acteam.vocago.domain.remote.NovelRemoteDataSource

class GetNovelFirstPageUseCase(
    private val novelRemoteDataSource: NovelRemoteDataSource,
) {
    suspend operator fun invoke(): Result<List<Novel>> {
        return novelRemoteDataSource.getFirstPageOnline()
    }

}