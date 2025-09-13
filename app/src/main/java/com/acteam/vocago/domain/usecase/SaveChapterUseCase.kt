package com.acteam.vocago.domain.usecase

import com.acteam.vocago.data.local.entity.ChapterEntity
import com.acteam.vocago.domain.local.NovelLocalDataSource

class SaveChapterUseCase(
    private val novelLocalDataSource: NovelLocalDataSource,
) {
    suspend operator fun invoke(chapter: ChapterEntity) {
        novelLocalDataSource.insertChapter(chapter)
    }
}