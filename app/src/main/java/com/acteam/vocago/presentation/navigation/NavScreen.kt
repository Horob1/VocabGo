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
    data class ChooseVocaListNavScreen(
        val word: String,
        val meaning: String,
        val pronunciation: String,
        val type: String,
    ) : NavScreen()

    @Serializable
    data class VocaListDetailNavScreen(val listId: Int) : NavScreen()

    @Serializable
    data class FlashCardNavScreen(val listId: Int) : NavScreen()

    @Serializable
    data object LearnNavScreen : NavScreen()

    @Serializable
    data object SettingNavScreen : NavScreen()

    @Serializable
    data object CameraNavScreen : NavScreen()

    @Serializable
    data class ToeicDetailNavScreen(val id: String) : NavScreen()

    @Serializable
    data class ToeicPartSelectionNavScreen(val id: String) : NavScreen()

    @Serializable
    data class ToeicPracticeNavScreen(val id: String, val selectedParts: String) : NavScreen()

    @Serializable
    data class ToeicResultsNavScreen(val id: String) : NavScreen()

    @Serializable
    data class ToeicResultDetailNavScreen(val id: String) : NavScreen()

    @Serializable
    data object ChangePasswordNavScreen : NavScreen()

    @Serializable
    data object DeviceManagementNavScreen : NavScreen()

    @Serializable
    data class VideoCallNavScreen(
        val receivedName: String,
        val avatarResId: Int,
        val videoResId: Int?,
    ) : NavScreen()

    @Serializable
    data class SearchNovelNavScreen(val keySearch: String) : NavScreen()

    @Serializable
    data class NovelDetailNavScreen(val novelId: String) : NavScreen()

    @Serializable
    data class ReadNovelNavScreen(val novelId: String, val chapterId: String) : NavScreen()

    @Serializable
    data class LearnVocaNavScreen(val listId: Int) : NavScreen()

    @Serializable
    data object NovelHistoryNavScreen : NavScreen()

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
    @Serializable
    data object UserNavNavigatorScreen : NavScreen()

    @Serializable
    data object AlarmNavScreen : NavScreen()

    @Serializable
    data object ProfileNavScreen : NavScreen()

    @Serializable
    data object BillingNavScreen : NavScreen()

    @Serializable
    data object AnalyzeUserNavScreen : NavScreen()

    @Serializable
    data object RankingNavScreen : NavScreen()

}