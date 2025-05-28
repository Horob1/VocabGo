package com.acteam.vocago.di

import com.acteam.vocago.data.remote.AuthRemoteDataSourceImpl
import com.acteam.vocago.data.remote.NewsRemoteDataSourceImpl
import com.acteam.vocago.data.remote.UserRemoteDataSourceImpl
import com.acteam.vocago.domain.remote.AuthRemoteDataSource
import com.acteam.vocago.domain.remote.NewsRemoteDataSource
import com.acteam.vocago.domain.remote.UserRemoteDataSource
import org.koin.core.qualifier.named
import org.koin.dsl.module

val remoteModule = module {
    single<AuthRemoteDataSource> {
        AuthRemoteDataSourceImpl(get(named(VOCAB_GO_BE_QUALIFIER)))
    }

    single<UserRemoteDataSource> {
        UserRemoteDataSourceImpl(get(named(VOCAB_GO_BE_QUALIFIER)))
    }

    single<NewsRemoteDataSource> {
        NewsRemoteDataSourceImpl(get(named(VOCAB_GO_BE_QUALIFIER)))
    }
}