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
    data class CommonChatNavScreen(val id: Int, val title: String, val avatarRes: Int) : NavScreen()

    @Serializable
    data class NewsHistoryNavScreen(
        val isBookmark: Boolean,
    ) : NavScreen()

    @Serializable
    data class NewsDetailNavScreen(
        val newsId: String,
    ) : NavScreen()

    @Serializable
    data object DictionaryNavScreen : NavScreen()

    @Serializable
    data class WordDetailNavScreen(val word: String) : NavScreen()

    @Serializable
    data class ChooseVocaListNavScreen(val word: String) : NavScreen()

    @Serializable
    data object SettingNavScreen : NavScreen()

    @Serializable
    data object CameraNavScreen : NavScreen()

    @Serializable
    data class VideoCallNavScreen(
        val receivedName: String,
        val avatarResId: Int,
        val videoResId: Int?
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

    // Mâm 2 của user
    data object UserNavNavigatorScreen : NavScreen()
    data object AlarmNavScreen : NavScreen()
    data object ProfileNavScreen : NavScreen()
    data object BillingNavScreen : NavScreen()
    data object AnalyzeUserNavScreen : NavScreen()

}