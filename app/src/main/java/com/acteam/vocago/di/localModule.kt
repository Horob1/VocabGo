package com.acteam.vocago.di

import com.acteam.vocago.data.local.encryptedpreferences.AuthEncryptedPreferencesImpl
import com.acteam.vocago.domain.local.AuthEncryptedPreferences
import org.koin.dsl.module

val localModule = module {
    single<AuthEncryptedPreferences> { AuthEncryptedPreferencesImpl(get()) }
}