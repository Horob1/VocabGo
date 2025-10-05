package com.acteam.vocago.di

import com.acteam.vocago.presentation.screen.auth.forgotpassword.ForgotPasswordViewModel
import com.acteam.vocago.presentation.screen.auth.login.LoginViewModel
import com.acteam.vocago.presentation.screen.auth.register.RegisterViewModel
import com.acteam.vocago.presentation.screen.auth.resetpassword.ResetPasswordViewModel
import com.acteam.vocago.presentation.screen.auth.verify2fa.VerifyTwoFAViewModel
import com.acteam.vocago.presentation.screen.auth.verifyemail.VerifyEmailViewModel
import com.acteam.vocago.presentation.screen.choosevoca.ChooseVocaViewModel
import com.acteam.vocago.presentation.screen.flashcard.FlashCardViewModel
import com.acteam.vocago.presentation.screen.learn.LearnViewModel
import com.acteam.vocago.presentation.screen.listennovel.ListenNovelViewModel
import com.acteam.vocago.presentation.screen.main.chat.ChatViewModel
import com.acteam.vocago.presentation.screen.main.news.NewsViewModel
import com.acteam.vocago.presentation.screen.main.novel.NovelViewModel
import com.acteam.vocago.presentation.screen.main.toeictest.ToeicViewModel
import com.acteam.vocago.presentation.screen.main.voca.VocaViewModel
import com.acteam.vocago.presentation.screen.newsdetail.NewsDetailViewModel
import com.acteam.vocago.presentation.screen.newshistory.NewsHistoryViewModel
import com.acteam.vocago.presentation.screen.noveldetail.NovelDetailViewModel
import com.acteam.vocago.presentation.screen.novelhistory.NovelHistoryViewModel
import com.acteam.vocago.presentation.screen.premium.PremiumViewModel
import com.acteam.vocago.presentation.screen.readnovel.ReadNovelViewModel
import com.acteam.vocago.presentation.screen.searchnovel.SearchNovelViewModel
import com.acteam.vocago.presentation.screen.setting.SettingViewModel
import com.acteam.vocago.presentation.screen.user.alarm.AlarmViewModel
import com.acteam.vocago.presentation.screen.user.profile.ProfileViewModel
import com.acteam.vocago.presentation.screen.user.ranking.RankingViewModel
import com.acteam.vocago.presentation.screen.user.usernavigator.UserNavigatorViewModel
import com.acteam.vocago.presentation.screen.vocalistdetail.VocaListDetailViewModel
import com.acteam.vocago.presentation.screen.welcome.WelcomeViewModel
import com.acteam.vocago.presentation.screen.worddetail.WordDetailViewModel
import com.acteam.vocago.presentation.socket.SocketViewModel
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
            get(), get(), get(), get(), get(), get(), get(), get(), get(), get()
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
            get(), get(), get()
        )
    }

    viewModel {
        UserNavigatorViewModel(
            get(), get()
        )
    }

    viewModel {
        AlarmViewModel(
            get(), get(), get(), get(), get()
        )
    }

    viewModel {
        ProfileViewModel(
            get(), get(), get(), get(), get(), get(), get(), get(), get(), get()
        )
    }
    viewModel {
        ToeicViewModel(
            get(), get(), get(), get(), get(), get(), get()
        )
    }

    viewModel {
        NovelViewModel(
            get(),
            get()
        )
    }

    viewModel {
        SearchNovelViewModel(get())
    }

    viewModel {
        NovelDetailViewModel(
            get(),
            get(),
            get()
        )
    }

    viewModel {
        ReadNovelViewModel(
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get()
        )
    }

    viewModel {
        NovelHistoryViewModel(
            get()
        )
    }

    viewModel {
        VocaViewModel(
            get(),
            get(),
            get(),
            get(),
            get(),
            get()
        )
    }

    viewModel {
        ChooseVocaViewModel(
            get(),
            get()
        )
    }

    viewModel {
        VocaListDetailViewModel(
            get(),
            get(),
            get(),
            get(),
        )
    }

    viewModel {
        FlashCardViewModel(get())
    }
    viewModel {
        LearnViewModel(get())
    }
    viewModel {
        RankingViewModel(get())
    }
    viewModel {
        ListenNovelViewModel(get(), get(), get())
    }

    viewModel {
        SocketViewModel(
            get(),
            get(),
            get(),
            get(),
        )
    }
    viewModel {
        PremiumViewModel(get())
    }


}