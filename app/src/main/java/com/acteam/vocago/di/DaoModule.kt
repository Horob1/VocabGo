package com.acteam.vocago.di

import androidx.room.Room
import com.acteam.vocago.data.local.AppDatabase
import org.koin.dsl.module

val daoModule = module {
    single {
        Room.databaseBuilder(
            get(),
            AppDatabase::class.java,
            "vocago_database"
        ).build()
    }

    single {
        get<AppDatabase>().userDao()
    }

    single {
        get<AppDatabase>().newsDao()
    }

    single {
        get<AppDatabase>().newsHistoryDao()
    }

    single {
        get<AppDatabase>().alarmDao()
    }

    single {
        get<AppDatabase>().localNovelHistoryDao()
    }

    single {
        get<AppDatabase>().vocaDao()
    }

}