package com.acteam.vocago.di

import com.acteam.vocago.data.repository.WelcomeRepositoryImpl
import com.acteam.vocago.domain.repository.WelcomeRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<WelcomeRepository> {
        WelcomeRepositoryImpl(get()) // get() sẽ lấy Context từ androidContext()
    }
}