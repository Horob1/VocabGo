package com.acteam.vocago.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.acteam.vocago.data.local.dao.LoggedInUserDao
import com.acteam.vocago.data.local.entity.LoggedInUser

@Database(entities = [LoggedInUser::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): LoggedInUserDao
}