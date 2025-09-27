package com.acteam.vocago.di

import com.acteam.vocago.data.repository.AlarmRepositoryImpl
import com.acteam.vocago.data.repository.LanguageRepositoryImpl
import com.acteam.vocago.data.repository.NewsRepositoryImpl
import com.acteam.vocago.data.repository.NovelRepositoryImpl
import com.acteam.vocago.data.repository.ReadNovelSettingRepositoryImpl
import com.acteam.vocago.data.repository.SystemConfigRepositoryImpl
import com.acteam.vocago.data.repository.TTSRepositoryImpl
import com.acteam.vocago.data.repository.VocaRepositoryImpl
import com.acteam.vocago.data.repository.WelcomeRepositoryImpl
import com.acteam.vocago.domain.repository.AlarmRepository
import com.acteam.vocago.domain.repository.LanguageRepository
import com.acteam.vocago.domain.repository.NewsRepository
import com.acteam.vocago.domain.repository.NovelRepository
import com.acteam.vocago.domain.repository.ReadNovelSettingRepository
import com.acteam.vocago.domain.repository.SystemConfigRepository
import com.acteam.vocago.domain.repository.TTSRepository
import com.acteam.vocago.domain.repository.VocaRepository
import com.acteam.vocago.domain.repository.WelcomeRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<WelcomeRepository> {
        WelcomeRepositoryImpl(get()) // get() sẽ lấy Context từ androidContext()
    }
    single<LanguageRepository> {
        LanguageRepositoryImpl(get())
    }
    single<SystemConfigRepository> {
        SystemConfigRepositoryImpl(get())
    }

    single<NewsRepository> {
        NewsRepositoryImpl(get(), get())
    }

    single<AlarmRepository> {
        AlarmRepositoryImpl(get())
    }

    single<NovelRepository> {
        NovelRepositoryImpl(get(), get())
    }

    single<ReadNovelSettingRepository> {
        ReadNovelSettingRepositoryImpl(get())
    }

    single<VocaRepository> {
        VocaRepositoryImpl(get())
    }

    single<TTSRepository> {
        TTSRepositoryImpl(get())
    }

}