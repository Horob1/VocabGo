package com.acteam.vocago.di

import com.acteam.vocago.domain.usecase.AnswerNewsUseCase
import com.acteam.vocago.domain.usecase.ChooseLanguageUserCase
import com.acteam.vocago.domain.usecase.CreateVocaListUseCase
import com.acteam.vocago.domain.usecase.DeleteAlarmByIdUseCase
import com.acteam.vocago.domain.usecase.DeleteVocaListUseCase
import com.acteam.vocago.domain.usecase.DeleteVocaUseCase
import com.acteam.vocago.domain.usecase.ForgotPasswordUseCase
import com.acteam.vocago.domain.usecase.GetAlarmByIdUseCase
import com.acteam.vocago.domain.usecase.GetAlarmListUseCase
import com.acteam.vocago.domain.usecase.GetAllVocaListUseCase
import com.acteam.vocago.domain.usecase.GetChapterUseCase
import com.acteam.vocago.domain.usecase.GetChosenNewsCategoriesUseCase
import com.acteam.vocago.domain.usecase.GetChosenNewsLevelUseCase
import com.acteam.vocago.domain.usecase.GetDictionaryWordUseCase
import com.acteam.vocago.domain.usecase.GetDynamicColorUseCase
import com.acteam.vocago.domain.usecase.GetFontFamilyUseCase
import com.acteam.vocago.domain.usecase.GetLanguageUseCase
import com.acteam.vocago.domain.usecase.GetLastReadChapterUseCase
import com.acteam.vocago.domain.usecase.GetLocalUserProfileUseCase
import com.acteam.vocago.domain.usecase.GetLoginStateUseCase
import com.acteam.vocago.domain.usecase.GetNewsDetailUseCase
import com.acteam.vocago.domain.usecase.GetNewsHistoryPagingUseCase
import com.acteam.vocago.domain.usecase.GetNewsPagingUseCase
import com.acteam.vocago.domain.usecase.GetNovelDetailUseCase
import com.acteam.vocago.domain.usecase.GetNovelFirstPageUseCase
import com.acteam.vocago.domain.usecase.GetReadNovelFirstPageUseCase
import com.acteam.vocago.domain.usecase.GetReadNovelFontSizeUseCase
import com.acteam.vocago.domain.usecase.GetReadNovelPagingFlowUseCase
import com.acteam.vocago.domain.usecase.GetReadNovelThemeUseCase
import com.acteam.vocago.domain.usecase.GetSearchNovelPagingFlowUseCase
import com.acteam.vocago.domain.usecase.GetStartScreenUseCase
import com.acteam.vocago.domain.usecase.GetThemeUseCase
import com.acteam.vocago.domain.usecase.GetVocaListDetailUseCase
import com.acteam.vocago.domain.usecase.InsertAlarmUseCase
import com.acteam.vocago.domain.usecase.LoadImageUseCase
import com.acteam.vocago.domain.usecase.LoginGoogleUseCase
import com.acteam.vocago.domain.usecase.LoginUseCase
import com.acteam.vocago.domain.usecase.LogoutUseCase
import com.acteam.vocago.domain.usecase.RegisterUseCase
import com.acteam.vocago.domain.usecase.ResendVerifyEmailUseCase
import com.acteam.vocago.domain.usecase.ResetPasswordUseCase
import com.acteam.vocago.domain.usecase.SaveOnBoardingStateUseCase
import com.acteam.vocago.domain.usecase.SaveWordToVocaListUseCase
import com.acteam.vocago.domain.usecase.SetDynamicColorUseCase
import com.acteam.vocago.domain.usecase.SetFontFamilyUseCase
import com.acteam.vocago.domain.usecase.SetNovelThemeUseCase
import com.acteam.vocago.domain.usecase.SetReadNovelFontSizeUseCase
import com.acteam.vocago.domain.usecase.SetThemeUseCase
import com.acteam.vocago.domain.usecase.SyncProfileUseCase
import com.acteam.vocago.domain.usecase.ToggleBookmarkNewsUseCase
import com.acteam.vocago.domain.usecase.UpdateAlarmUseCase
import com.acteam.vocago.domain.usecase.UpdateAvatarUseCase
import com.acteam.vocago.domain.usecase.UpdateChosenCategoriesUseCase
import com.acteam.vocago.domain.usecase.UpdateChosenNewsLevelUseCase
import com.acteam.vocago.domain.usecase.UpdateProfileUseCase
import com.acteam.vocago.domain.usecase.UpdateReadChapterUseCase
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
        LoginGoogleUseCase(get(), get())
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

    single {
        AnswerNewsUseCase(get())
    }

    single {
        ToggleBookmarkNewsUseCase(get())
    }

    single {
        GetNewsHistoryPagingUseCase(get())
    }

    single {
        GetDictionaryWordUseCase(get())
    }

    single {
        LogoutUseCase(
            get(), get(), get()
        )
    }
    // alarm
    single {
        GetAlarmListUseCase(get())
    }
    single {
        GetAlarmByIdUseCase(get())
    }
    single {
        InsertAlarmUseCase(get(), get())
    }
    single {
        UpdateAlarmUseCase(get(), get())
    }
    single {
        DeleteAlarmByIdUseCase(get(), get())
    }
    single {
        UpdateAvatarUseCase(
            get(),
        )
    }
    single {
        UpdateProfileUseCase(
            get()
        )
    }

    single {
        GetNovelFirstPageUseCase(get())
    }

    single {
        GetSearchNovelPagingFlowUseCase(get())
    }

    single {
        GetNovelDetailUseCase(get())
    }

    single {
        GetChapterUseCase(get())
    }

    single {
        SetNovelThemeUseCase(get())
    }

    single {
        SetReadNovelFontSizeUseCase(get())
    }

    single {
        GetReadNovelThemeUseCase(get())
    }

    single {
        GetReadNovelFontSizeUseCase(get())
    }

    single {
        GetFontFamilyUseCase(get())
    }

    single {
        SetFontFamilyUseCase(get())
    }

    single {
        GetLastReadChapterUseCase(get())
    }

    single {
        UpdateReadChapterUseCase(get())
    }

    single {
        GetReadNovelPagingFlowUseCase(get())
    }

    factory {
        GetReadNovelFirstPageUseCase(get())
    }

    single {
        GetAllVocaListUseCase(get())
    }

    single {
        CreateVocaListUseCase(get())
    }

    single {
        SaveWordToVocaListUseCase(get())
    }

    single {
        GetVocaListDetailUseCase(get())
    }

    single {
        LoadImageUseCase(get())
    }

    single {
        DeleteVocaUseCase(get())
    }

    single {
        DeleteVocaListUseCase(get())
    }
}