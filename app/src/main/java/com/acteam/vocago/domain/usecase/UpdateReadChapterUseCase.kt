package com.acteam.vocago.domain.usecase

import com.acteam.vocago.data.local.entity.LocalNovelHistoryEntity
import com.acteam.vocago.domain.local.NovelLocalDataSource

class UpdateReadChapterUseCase(
    private val novelLocalDataSource: NovelLocalDataSource,
) {
    suspend operator fun invoke(novelId: String, chapterId: String) {
        val localNovelHistoryEntity = LocalNovelHistoryEntity(novelId, chapterId)
        novelLocalDataSource.insertLocalNovelHistory(localNovelHistoryEntity)
    }

}