package com.acteam.vocago.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.acteam.vocago.data.local.entity.NovelEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NovelDao {
    @Query("SELECT * FROM novels")
    fun getFlowAllNovels(): Flow<List<NovelEntity>>

    @Query("SELECT * FROM novels WHERE _id = :novelId")
    fun getFlowNovelById(novelId: String): Flow<NovelEntity?>

    @Query("SELECT * FROM novels ")
    suspend fun getAllNovels(): List<NovelEntity>

    @Query("SELECT * FROM novels LIMIT :limit")
    suspend fun getNovelsLimit(limit: Int): List<NovelEntity>

    @Query("SELECT * FROM novels WHERE _id = :novelId")
    suspend fun getNovelById(novelId: String): NovelEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNovel(novel: NovelEntity)

    @Query("DELETE FROM novels WHERE _id = :novelId")
    suspend fun deleteNovelById(novelId: String)

    @Query("DELETE FROM novels")
    suspend fun deleteAllNovels()
}