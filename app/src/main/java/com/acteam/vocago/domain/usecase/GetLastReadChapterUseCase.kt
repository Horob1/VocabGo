package com.acteam.vocago.domain.usecase

import com.acteam.vocago.data.local.entity.LocalNovelHistoryEntity
import com.acteam.vocago.domain.local.NovelLocalDataSource

class GetLastReadChapterUseCase(
    private val novelLocalDataSource: NovelLocalDataSource,
) {
    suspend operator fun invoke(novelId: String): LocalNovelHistoryEntity? {
        return novelLocalDataSource.findLastReadChapter(novelId)
    }
}