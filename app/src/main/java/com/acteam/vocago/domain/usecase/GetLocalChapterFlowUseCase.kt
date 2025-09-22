package com.acteam.vocago.domain.usecase

import com.acteam.vocago.domain.local.NovelLocalDataSource

class GetLocalChapterFlowUseCase(
    private val novelLocalDataSource: NovelLocalDataSource,
) {
    operator fun invoke(novelId: String) = novelLocalDataSource.getChaptersByFictionId(novelId)
}