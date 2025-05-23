package com.acteam.vocago.di

import com.acteam.vocago.data.local.AuthEncryptedPreferencesImpl
import com.acteam.vocago.domain.local.AuthEncryptedPreferences
import org.koin.dsl.module

val localDataModule = module {
    single<AuthEncryptedPreferences> { AuthEncryptedPreferencesImpl(get()) }
}