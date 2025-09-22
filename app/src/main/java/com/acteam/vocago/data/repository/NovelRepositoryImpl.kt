package com.acteam.vocago.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.acteam.vocago.data.model.ChapterDto
import com.acteam.vocago.data.model.ChapterPayload
import com.acteam.vocago.data.model.NearChapterPayload
import com.acteam.vocago.data.paging.NovelPagingSource
import com.acteam.vocago.data.paging.ReadNovelPagingSource
import com.acteam.vocago.domain.local.NovelLocalDataSource
import com.acteam.vocago.domain.model.Novel
import com.acteam.vocago.domain.model.NovelDetail
import com.acteam.vocago.domain.model.NovelDetailChapter
import com.acteam.vocago.domain.remote.NovelRemoteDataSource
import com.acteam.vocago.domain.repository.NovelRepository
import kotlinx.coroutines.flow.Flow

class NovelRepositoryImpl(
    private val novelRemoteDataSource: NovelRemoteDataSource,
    private val novelLocalDataSource: NovelLocalDataSource,
) : NovelRepository {
    override fun getNovelPagingFlow(keySearch: String): Flow<PagingData<Novel>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { NovelPagingSource(novelRemoteDataSource, keySearch) }
        ).flow
    }

    override fun getReadNovelPagingFlow(): Flow<PagingData<Novel>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { ReadNovelPagingSource(novelRemoteDataSource) }
        ).flow
    }

    override suspend fun getNovelDetail(id: String): Result<NovelDetail> {
        val remoteData = novelRemoteDataSource.getNovelDetail(id)

        if (remoteData.isSuccess) {
            val novelDetail = remoteData.getOrNull()
            if (novelDetail != null) {
                // lưu metadata vào local
                novelLocalDataSource.insertNovelMetaData(novelDetail.toNovel())
                return Result.success(novelDetail.toNovelDetail())
            }
            return Result.failure(Exception("Remote returned null novel detail"))
        } else {
            val localData = novelLocalDataSource.getNovelMetaData(id)
            val localChapter = novelLocalDataSource.getFullChaptersByFictionId(id).map {
                NovelDetailChapter(
                    _id = it._id,
                    chapterNumber = it.chapterNumber,
                    chapterTitle = it.chapterTitle,
                    createdAt = it.createdAt,
                )
            }
            if (localData != null) {
                return Result.success(
                    NovelDetail(
                        _id = localData._id,
                        fictionNumber = localData.fictionNumber,
                        fictionTitle = localData.fictionTitle,
                        totalChapters = localData.totalChapters,
                        image = localData.image,
                        author = localData.author,
                        chapters = localChapter
                    )
                )
            }
            return Result.failure(Exception("Failed to fetch novel detail from both remote and local"))
        }
    }


    override suspend fun saveNovel(novel: NovelDetail) {
        novelLocalDataSource.insertNovelMetaData(
            Novel(
                _id = novel._id,
                fictionNumber = novel.fictionNumber,
                fictionTitle = novel.fictionTitle,
                totalChapters = novel.totalChapters,
                image = novel.image,
                author = novel.author,
                isPublish = true,
                createdAt = "",
                updatedAt = ""
            )
        )
    }

    override suspend fun getReadNovelFirstPage(): List<Novel> {
        return novelLocalDataSource.getReadNovelFirstPage()
    }

    override fun getReadNovelFlow(): Flow<List<Novel>> {
        return novelLocalDataSource.getReadNovelFlow()
    }

    override suspend fun getChapterDetail(chapterId: String): Result<ChapterDto> {
        val localData = novelLocalDataSource.getChapterById(
            chapterId
        )
        if (localData != null) {
            return Result.success(
                ChapterDto(
                    chapter = ChapterPayload(
                        _id = localData._id,
                        chapterNumber = localData.chapterNumber,
                        chapterTitle = localData.chapterTitle,
                        content = localData.content,
                        fictionId = localData.fictionId,
                        createdAt = localData.createdAt,
                    ),
                    previousChapter = if (localData.previousChapterId != null) NearChapterPayload(
                        _id = localData.previousChapterId,
                        chapterNumber = localData.chapterNumber - 1,
                        chapterTitle = "",
                    ) else null,
                    nextChapter = if (localData.nextChapterId != null) NearChapterPayload(
                        _id = localData.nextChapterId,
                        chapterNumber = localData.chapterNumber - 1,
                        chapterTitle = "",
                    ) else null,
                )

            )
        } else {
            val remoteData = novelRemoteDataSource.getChapterDetail(chapterId)
            if (remoteData.isSuccess) {
                val chapter = remoteData.getOrNull()
                if (chapter != null) {
                    return Result.success(chapter)
                }
            }

            return Result.failure(Exception("Failed to fetch chapter detail from both remote and local"))
        }
    }
}