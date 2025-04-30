package com.acteam.vocago.di

import com.acteam.vocago.domain.usecase.GetStartScreenUseCase
import com.acteam.vocago.domain.usecase.SaveOnBoardingStateUseCase
import org.koin.dsl.module

val useCaseModule = module {
    // Use case for welcome screen
    factory { SaveOnBoardingStateUseCase(get()) }
    factory { GetStartScreenUseCase(get()) }
}