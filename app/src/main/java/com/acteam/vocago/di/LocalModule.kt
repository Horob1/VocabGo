package com.acteam.vocago.di

import com.acteam.vocago.data.local.AuthLocalDataSourceImpl
import com.acteam.vocago.data.local.UserLocalDataSourceImpl
import com.acteam.vocago.domain.local.AuthLocalDataSource
import com.acteam.vocago.domain.local.UserLocalDataSource
import org.koin.dsl.module

val localDataModule = module {
    single<AuthLocalDataSource> { AuthLocalDataSourceImpl(get()) }
    single<UserLocalDataSource> {
        UserLocalDataSourceImpl(get())
    }
}