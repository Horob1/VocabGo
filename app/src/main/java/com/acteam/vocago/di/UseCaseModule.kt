package com.acteam.vocago.di

import com.acteam.vocago.domain.usecase.ChooseLanguageUserCase
import com.acteam.vocago.domain.usecase.ForgotPasswordUseCase
import com.acteam.vocago.domain.usecase.GetChosenNewsCategoriesUseCase
import com.acteam.vocago.domain.usecase.GetChosenNewsLevelUseCase
import com.acteam.vocago.domain.usecase.GetDynamicColorUseCase
import com.acteam.vocago.domain.usecase.GetLanguageUseCase
import com.acteam.vocago.domain.usecase.GetLocalUserProfileUseCase
import com.acteam.vocago.domain.usecase.GetLoginStateUseCase
import com.acteam.vocago.domain.usecase.GetNewsDetailUseCase
import com.acteam.vocago.domain.usecase.GetNewsPagingUseCase
import com.acteam.vocago.domain.usecase.GetStartScreenUseCase
import com.acteam.vocago.domain.usecase.GetThemeUseCase
import com.acteam.vocago.domain.usecase.LoginUseCase
import com.acteam.vocago.domain.usecase.RegisterUseCase
import com.acteam.vocago.domain.usecase.ResendVerifyEmailUseCase
import com.acteam.vocago.domain.usecase.ResetPasswordUseCase
import com.acteam.vocago.domain.usecase.SaveOnBoardingStateUseCase
import com.acteam.vocago.domain.usecase.SetDynamicColorUseCase
import com.acteam.vocago.domain.usecase.SetThemeUseCase
import com.acteam.vocago.domain.usecase.SyncProfileUseCase
import com.acteam.vocago.domain.usecase.UpdateChosenCategoriesUseCase
import com.acteam.vocago.domain.usecase.UpdateChosenNewsLevelUseCase
import com.acteam.vocago.domain.usecase.VerifyEmailUseCase
import com.acteam.vocago.domain.usecase.VerifyTwoFAUseCase
import org.koin.dsl.module

val useCaseModule = module {
    // Use case for welcome screen
    single { SaveOnBoardingStateUseCase(get()) }

    single { GetStartScreenUseCase(get()) }

    single { GetLanguageUseCase(get()) }

    single { GetThemeUseCase(get()) }

    single { ChooseLanguageUserCase(get()) }

    single {
        LoginUseCase(get(), get())
    }
    single {
        ForgotPasswordUseCase(get())
    }
    single {
        ResetPasswordUseCase(get())
    }
    single {
        RegisterUseCase(get())
    }
    single {
        VerifyEmailUseCase(get())
    }
    single {
        ResendVerifyEmailUseCase(get())
    }
    single {
        VerifyTwoFAUseCase(get(), get())
    }

    single {
        GetLoginStateUseCase(get())
    }

    single {
        GetLocalUserProfileUseCase(get())
    }

    single {
        SyncProfileUseCase(get(), get())
    }

    single {
        UpdateChosenCategoriesUseCase(get())
    }

    single {
        GetChosenNewsCategoriesUseCase(get())
    }

    single {
        GetChosenNewsLevelUseCase(get())
    }

    single {
        UpdateChosenNewsLevelUseCase(get())
    }

    single {
        GetDynamicColorUseCase(get())
    }

    single {
        GetNewsPagingUseCase(get())
    }

    single {
        SetDynamicColorUseCase(get())
    }

    single {
        SetThemeUseCase(get())
    }

    single {
        GetNewsDetailUseCase(get())
    }
}