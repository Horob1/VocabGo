package com.acteam.vocago.di

import com.acteam.vocago.presentation.screen.auth.forgotpassword.ForgotPasswordViewModel
import com.acteam.vocago.presentation.screen.auth.login.LoginViewModel
import com.acteam.vocago.presentation.screen.auth.register.RegisterViewModel
import com.acteam.vocago.presentation.screen.auth.resetpassword.ResetPasswordViewModel
import com.acteam.vocago.presentation.screen.auth.verify2fa.VerifyTwoFAViewModel
import com.acteam.vocago.presentation.screen.auth.verifyemail.VerifyEmailViewModel
import com.acteam.vocago.presentation.screen.main.chat.ChatViewModel
import com.acteam.vocago.presentation.screen.main.news.NewsViewModel
import com.acteam.vocago.presentation.screen.newsdetail.NewsDetailViewModel
import com.acteam.vocago.presentation.screen.newshistory.NewsHistoryViewModel
import com.acteam.vocago.presentation.screen.setting.SettingViewModel
import com.acteam.vocago.presentation.screen.user.usernavigator.UserNavigatorViewModel
import com.acteam.vocago.presentation.screen.welcome.WelcomeViewModel
import com.acteam.vocago.presentation.screen.worddetail.WordDetailViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        WelcomeViewModel(get(), get(), get())
    }
    viewModel {
        LoginViewModel(
            get(), get()
        )
    }
    viewModel {
        RegisterViewModel(
            get()
        )
    }
    viewModel {
        ForgotPasswordViewModel(
            get()
        )
    }
    viewModel {
        ResetPasswordViewModel(
            get(), get()
        )
    }
    viewModel {
        VerifyEmailViewModel(
            get(), get()
        )
    }
    viewModel {
        VerifyTwoFAViewModel(
            get()
        )
    }

    viewModel {
        NewsViewModel(
            get(), get(), get(), get(), get(), get(), get(), get()
        )
    }

    viewModel {
        SettingViewModel(
            get(), get(), get(), get(), get(), get()
        )
    }

    viewModel {
        NewsDetailViewModel(
            get(), get(), get(), get(), get()
        )
    }
    viewModel {
        ChatViewModel(
            get(), get()
        )
    }

    viewModel {
        NewsHistoryViewModel(
            get()
        )
    }

    viewModel {
        WordDetailViewModel(
            get()
        )
    }

    viewModel {
        UserNavigatorViewModel(
            get(), get()
        )
    }
}