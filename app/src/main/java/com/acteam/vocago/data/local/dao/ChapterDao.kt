package com.acteam.vocago.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.acteam.vocago.data.local.entity.ChapterEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ChapterDao {
    @Query("SELECT _id FROM chapters WHERE fictionId = :fictionId")
    fun getChaptersByFictionId(fictionId: String): Flow<List<String>>

    @Query("SELECT * FROM chapters WHERE _id = :chapterId")
    suspend fun getChapterById(chapterId: String): ChapterEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChapters(chapters: List<ChapterEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChapter(chapter: ChapterEntity)

    @Query("SELECT * FROM chapters WHERE fictionId = :fictionId")
    suspend fun getFullChaptersByFictionId(fictionId: String): List<ChapterEntity>

}