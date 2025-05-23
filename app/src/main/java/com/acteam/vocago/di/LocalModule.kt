package com.acteam.vocago.di

import com.acteam.vocago.data.local.AuthEncryptedPreferencesImpl
import com.acteam.vocago.data.local.UserLocalDataSourceImpl
import com.acteam.vocago.domain.local.AuthEncryptedPreferences
import com.acteam.vocago.domain.local.UserLocalDataSource
import org.koin.dsl.module

val localDataModule = module {
    single<AuthEncryptedPreferences> { AuthEncryptedPreferencesImpl(get()) }
    single<UserLocalDataSource> {
        UserLocalDataSourceImpl(get())
    }
}