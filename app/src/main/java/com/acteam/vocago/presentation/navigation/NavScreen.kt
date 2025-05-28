package com.acteam.vocago.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class NavScreen {

    // Mâm 1
    @Serializable
    data object WelcomeNavScreen : NavScreen()

    @Serializable
    data object AuthNavScreen : NavScreen()

    @Serializable
    data object MainNavScreen : NavScreen()

    @Serializable
    data object UserNavScreen : NavScreen()

    @Serializable
    data class NewsHistoryNavScreen(
        val isBookmark: Boolean,
    ) : NavScreen()

    @Serializable
    data object DictionaryNavScreen : NavScreen()

    @Serializable
    data object SettingNavScreen : NavScreen()

    @Serializable
    data class NewsDetailNavScreen(
        val newsId: Int,
    ) : NavScreen()

    // Mâm 2
    // Mân 2 của Auth
    @Serializable
    data object LoginNavScreen : NavScreen()

    @Serializable
    data object RegisterNavScreen : NavScreen()

    @Serializable
    data object ForgotPasswordNavScreen : NavScreen()

    @Serializable
    data class ResetPasswordNavScreen(val email: String) : NavScreen()

    @Serializable
    data class VerifyEmailNavScreen(val email: String) : NavScreen()

    @Serializable
    data class VerifyTwoFANavScreen(val email: String) : NavScreen()

    //Mâm 2 của Main
    @Serializable
    data object NewsNavScreen : NavScreen()

    @Serializable
    data object NovelNavScreen : NavScreen()

    @Serializable
    data object VocaNavScreen : NavScreen()

    @Serializable
    data object ToeicNavScreen : NavScreen()

    @Serializable
    data object ChatNavScreen : NavScreen()

    // Mâm 3
}