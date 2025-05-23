package com.acteam.vocago.domain.model

import androidx.annotation.RawRes
import androidx.annotation.StringRes
import com.acteam.vocago.R
import com.acteam.vocago.presentation.navigation.NavScreen

sealed class NavBottomBarItem(
    val route: NavScreen,
    @StringRes val title: Int,
    @RawRes val icon: Int,
) {
    data object News : NavBottomBarItem(
        route = NavScreen.NewsNavScreen,
        title = R.string.news,
        icon = R.raw.home
    )

    data object Novel : NavBottomBarItem(
        route = NavScreen.NovelNavScreen,
        title = R.string.novel,
        icon = R.raw.novel
    )

    data object Voca : NavBottomBarItem(
        route = NavScreen.VocaNavScreen,
        title = R.string.voca,
        icon = R.raw.voca
    )

    data object Toeic : NavBottomBarItem(
        route = NavScreen.ToeicNavScreen,
        title = R.string.toeic,
        icon = R.raw.test
    )

    data object Chat : NavBottomBarItem(
        route = NavScreen.ChatNavScreen,
        title = R.string.chat,
        icon = R.raw.chat
    )

}