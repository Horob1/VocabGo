package com.acteam.vocago.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.acteam.vocago.data.local.entity.NewsEntity

@Dao
interface NewsDao {
    @Query("SELECT * FROM news ORDER BY autoId ASC")
    fun getAllNewsInInsertOrder(): PagingSource<Int, NewsEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertNews(newsList: List<NewsEntity>)

    // 3. Xóa tất cả news (ví dụ khi refresh data)
    @Query("DELETE FROM news")
    suspend fun clearAllNews()

}