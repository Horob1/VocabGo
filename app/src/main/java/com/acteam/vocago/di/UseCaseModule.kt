package com.acteam.vocago.di

import com.acteam.vocago.domain.usecase.ChooseLanguageUserCase
import com.acteam.vocago.domain.usecase.ForgotPasswordUseCase
import com.acteam.vocago.domain.usecase.GetLanguageUseCase
import com.acteam.vocago.domain.usecase.GetStartScreenUseCase
import com.acteam.vocago.domain.usecase.GetThemeUseCase
import com.acteam.vocago.domain.usecase.LoginUseCase
import com.acteam.vocago.domain.usecase.SaveOnBoardingStateUseCase
import org.koin.dsl.module

val useCaseModule = module {
    // Use case for welcome screen
    factory { SaveOnBoardingStateUseCase(get()) }

    factory { GetStartScreenUseCase(get()) }

    single { GetLanguageUseCase(get()) }

    single { GetThemeUseCase(get()) }

    single { ChooseLanguageUserCase(get()) }

    factory {
        LoginUseCase(get(), get())
    }

    factory {
        ForgotPasswordUseCase(get())
    }
}