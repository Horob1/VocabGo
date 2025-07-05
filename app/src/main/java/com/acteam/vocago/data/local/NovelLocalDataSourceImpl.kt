package com.acteam.vocago.data.local

import com.acteam.vocago.data.local.dao.LocalNovelHistoryDao
import com.acteam.vocago.data.local.entity.LocalNovelHistoryEntity
import com.acteam.vocago.domain.local.NovelLocalDataSource

class NovelLocalDataSourceImpl(
    private val localNovelHistoryDao: LocalNovelHistoryDao,
) : NovelLocalDataSource {
    override suspend fun findLastReadChapter(novelId: String): LocalNovelHistoryEntity? {
        return localNovelHistoryDao.findLastReadChapter(novelId)
    }

    override suspend fun insertLocalNovelHistory(localNovelHistoryEntity: LocalNovelHistoryEntity) {
        localNovelHistoryDao.insertLocalNovelHistory(localNovelHistoryEntity)
    }
}