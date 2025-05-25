package com.acteam.vocago.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.acteam.vocago.data.local.entity.LoggedInUser
import kotlinx.coroutines.flow.Flow

@Dao
interface LoggedInUserDao {

    @Insert(onConflict = REPLACE)
    suspend fun insert(user: LoggedInUser)

    @Query("SELECT * FROM logged_in_user LIMIT 1")
    fun getUser(): Flow<LoggedInUser?>

    @Query("DELETE FROM logged_in_user")
    suspend fun clear()
}
