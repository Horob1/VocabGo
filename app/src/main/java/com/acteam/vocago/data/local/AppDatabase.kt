package com.acteam.vocago.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.acteam.vocago.data.local.dao.LoggedInUserDao
import com.acteam.vocago.data.local.dao.NewsDao
import com.acteam.vocago.data.local.entity.LoggedInUser
import com.acteam.vocago.data.local.entity.NewsEntity
import com.acteam.vocago.utils.RoomConverters

@Database(entities = [LoggedInUser::class, NewsEntity::class], version = 1)
@TypeConverters(RoomConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): LoggedInUserDao
    abstract fun newsDao(): NewsDao

}