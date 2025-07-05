package com.acteam.vocago.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.acteam.vocago.data.local.dao.AlarmDao
import com.acteam.vocago.data.local.dao.LocalNovelHistoryDao
import com.acteam.vocago.data.local.dao.LoggedInUserDao
import com.acteam.vocago.data.local.dao.NewsDao
import com.acteam.vocago.data.local.dao.NewsHistoryDao
import com.acteam.vocago.data.local.dao.VocaDao
import com.acteam.vocago.data.local.entity.AlarmEntity
import com.acteam.vocago.data.local.entity.LocalNovelHistoryEntity
import com.acteam.vocago.data.local.entity.LoggedInUser
import com.acteam.vocago.data.local.entity.NewsEntity
import com.acteam.vocago.data.local.entity.NewsHistoryEntity
import com.acteam.vocago.data.local.entity.VocaEntity
import com.acteam.vocago.data.local.entity.VocaListEntity
import com.acteam.vocago.utils.RoomConverters

@Database(
    entities = [
        LoggedInUser::class,
        NewsEntity::class,
        NewsHistoryEntity::class,
        AlarmEntity::class,
        LocalNovelHistoryEntity::class,
        VocaListEntity::class,
        VocaEntity::class
    ],
    version = 1
)
@TypeConverters(RoomConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun alarmDao(): AlarmDao
    abstract fun userDao(): LoggedInUserDao
    abstract fun newsDao(): NewsDao
    abstract fun newsHistoryDao(): NewsHistoryDao
    abstract fun localNovelHistoryDao(): LocalNovelHistoryDao
    abstract fun vocaDao(): VocaDao
}