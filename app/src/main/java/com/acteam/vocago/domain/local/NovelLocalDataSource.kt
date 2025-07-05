package com.acteam.vocago.domain.local

import com.acteam.vocago.data.local.entity.LocalNovelHistoryEntity

interface NovelLocalDataSource {
    suspend fun findLastReadChapter(novelId: String): LocalNovelHistoryEntity?
    suspend fun insertLocalNovelHistory(localNovelHistoryEntity: LocalNovelHistoryEntity)
}