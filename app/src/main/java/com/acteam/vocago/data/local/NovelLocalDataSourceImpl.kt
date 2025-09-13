package com.acteam.vocago.data.local

import com.acteam.vocago.data.local.dao.ChapterDao
import com.acteam.vocago.data.local.dao.LocalNovelHistoryDao
import com.acteam.vocago.data.local.dao.NovelDao
import com.acteam.vocago.data.local.entity.ChapterEntity
import com.acteam.vocago.data.local.entity.LocalNovelHistoryEntity
import com.acteam.vocago.data.local.entity.NovelEntity
import com.acteam.vocago.domain.local.NovelLocalDataSource
import com.acteam.vocago.domain.model.Novel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NovelLocalDataSourceImpl(
    private val localNovelHistoryDao: LocalNovelHistoryDao,
    private val localNovelDao: NovelDao,
    private val chapterDao: ChapterDao,
) : NovelLocalDataSource {
    override suspend fun findLastReadChapter(novelId: String): LocalNovelHistoryEntity? {
        return localNovelHistoryDao.findLastReadChapter(novelId)
    }

    override suspend fun insertLocalNovelHistory(localNovelHistoryEntity: LocalNovelHistoryEntity) {
        localNovelHistoryDao.insertLocalNovelHistory(localNovelHistoryEntity)
    }

    override suspend fun getNovelMetaData(novelId: String): Novel? {
        return localNovelDao.getNovelById(novelId)?.toNovel()
    }

    override suspend fun insertNovelMetaData(novel: Novel) {
        localNovelDao.insertNovel(
            NovelEntity(
                _id = novel._id,
                fictionNumber = novel.fictionNumber,
                fictionTitle = novel.fictionTitle,
                totalChapters = novel.totalChapters,
                image = novel.image,
                author = novel.author,
                isPublish = novel.isPublish,
                createdAt = novel.createdAt,
                updatedAt = novel.updatedAt,
            )
        )
    }

    override suspend fun getReadNovelFirstPage(): List<Novel> {
        return localNovelDao.getNovelsLimit(5).map { it.toNovel() }
    }

    override fun getReadNovelFlow(): Flow<List<Novel>> {
        return localNovelDao.getFlowAllNovels().map {
            it.map { novelEntity -> novelEntity.toNovel() }
        }
    }

    override suspend fun getChapterById(chapterId: String): ChapterEntity? {
        return chapterDao.getChapterById(chapterId)
    }

    override suspend fun insertChapters(chapters: List<ChapterEntity>) {
        chapterDao.insertChapters(chapters)
    }

    override suspend fun insertChapter(chapter: ChapterEntity) {
        chapterDao.insertChapter(chapter)
    }

    override fun getChaptersByFictionId(fictionId: String): Flow<List<String>> {
        return chapterDao.getChaptersByFictionId(fictionId)
    }

    override suspend fun getFullChaptersByFictionId(fictionId: String): List<ChapterEntity> {
        return chapterDao.getFullChaptersByFictionId(fictionId)
    }
}