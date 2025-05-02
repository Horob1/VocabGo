package com.acteam.vocago.di

import com.acteam.vocago.network.VOCAB_GO_BE_QUALIFIER
import com.acteam.vocago.network.vocabGoBeNetwork
import org.koin.core.qualifier.named
import org.koin.dsl.module

val networkModule = module {
    single(named(VOCAB_GO_BE_QUALIFIER)) {
        vocabGoBeNetwork
    }
}
