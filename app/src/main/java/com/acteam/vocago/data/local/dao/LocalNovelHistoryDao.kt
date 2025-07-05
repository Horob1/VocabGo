package com.acteam.vocago.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.acteam.vocago.data.local.entity.LocalNovelHistoryEntity

@Dao
interface LocalNovelHistoryDao {
    @Query("SELECT * FROM local_novel_history WHERE novelId = :novelId ORDER BY chapterId DESC LIMIT 1")
    suspend fun findLastReadChapter(novelId: String): LocalNovelHistoryEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocalNovelHistory(localNovelHistoryEntity: LocalNovelHistoryEntity)
}