package com.acteam.vocago.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.acteam.vocago.data.local.entity.NewsHistoryEntity

@Dao
interface NewsHistoryDao {
    @Query("SELECT * FROM news_history ORDER BY autoId ASC")
    fun getAllNewsHistoryInInsertOrder(): PagingSource<Int, NewsHistoryEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertNews(newsList: List<NewsHistoryEntity>)

    @Query("DELETE FROM news_history")
    suspend fun clearAllNews()
}