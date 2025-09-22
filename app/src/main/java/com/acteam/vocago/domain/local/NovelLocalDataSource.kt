package com.acteam.vocago.domain.local

import com.acteam.vocago.data.local.entity.ChapterEntity
import com.acteam.vocago.data.local.entity.LocalNovelHistoryEntity
import com.acteam.vocago.domain.model.Novel
import kotlinx.coroutines.flow.Flow

interface NovelLocalDataSource {
    suspend fun findLastReadChapter(novelId: String): LocalNovelHistoryEntity?
    suspend fun insertLocalNovelHistory(localNovelHistoryEntity: LocalNovelHistoryEntity)

    suspend fun getNovelMetaData(novelId: String): Novel?
    suspend fun insertNovelMetaData(novel: Novel)

    suspend fun getReadNovelFirstPage(): List<Novel>

    fun getReadNovelFlow(): Flow<List<Novel>>

    // chapter
    suspend fun getChapterById(chapterId: String): ChapterEntity?
    suspend fun insertChapters(chapters: List<ChapterEntity>)
    suspend fun insertChapter(chapter: ChapterEntity)
    fun getChaptersByFictionId(fictionId: String): Flow<List<String>>

    suspend fun getFullChaptersByFictionId(fictionId: String): List<ChapterEntity>

}