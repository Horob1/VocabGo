package com.acteam.vocago.di

import com.acteam.vocago.presentation.screen.auth.login.LoginViewModel
import com.acteam.vocago.presentation.screen.welcome.WelcomeViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        WelcomeViewModel(get(), get(), get())
    }
    viewModel {
        LoginViewModel()
    }
}