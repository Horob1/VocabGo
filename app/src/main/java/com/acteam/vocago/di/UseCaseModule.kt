package com.acteam.vocago.di

import com.acteam.vocago.domain.usecase.ChooseLanguageUserCase
import com.acteam.vocago.domain.usecase.ForgotPasswordUseCase
import com.acteam.vocago.domain.usecase.GetLanguageUseCase
import com.acteam.vocago.domain.usecase.GetStartScreenUseCase
import com.acteam.vocago.domain.usecase.GetThemeUseCase
import com.acteam.vocago.domain.usecase.LoginUseCase
import com.acteam.vocago.domain.usecase.RegisterUseCase
import com.acteam.vocago.domain.usecase.ResendVerifyEmailUseCase
import com.acteam.vocago.domain.usecase.ResetPasswordUseCase
import com.acteam.vocago.domain.usecase.SaveOnBoardingStateUseCase
import com.acteam.vocago.domain.usecase.VerifyEmailUseCase
import com.acteam.vocago.domain.usecase.VerifyTwoFAUseCase
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
    factory {
        ResetPasswordUseCase(get())
    }
    factory {
        RegisterUseCase(get())
    }
    factory {
        VerifyEmailUseCase(get())
    }
    factory {
        ResendVerifyEmailUseCase(get())
    }
    factory {
        VerifyTwoFAUseCase(get(), get())
    }
//    factory {
//        RefreshTokenUseCase(get(), get())
//    }
}