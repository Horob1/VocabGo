package com.acteam.vocago.di

import com.acteam.vocago.data.remote.AuthRemoteDataSourceImpl
import com.acteam.vocago.domain.remote.AuthRemoteDataSource
import org.koin.core.qualifier.named
import org.koin.dsl.module

val remoteModule = module {
    single<AuthRemoteDataSource> {
        AuthRemoteDataSourceImpl(
            get(
                named(VOCAB_GO_BE_QUALIFIER)
            )
        )
    }
}