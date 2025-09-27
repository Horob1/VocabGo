package com.acteam.vocago.utils

sealed class VocaGoRoutes(val path: String) {
    data object Login : VocaGoRoutes("api/v1/user/login")
    data object Register : VocaGoRoutes("api/v1/user/register")
    data object ForgotPassword : VocaGoRoutes("api/v1/user/forgot-password")
    data object ResetPassword : VocaGoRoutes("api/v1/user/reset-password")
    data object RefreshToken : VocaGoRoutes("api/v1/user/refresh-token")
    data object VerifyEmail : VocaGoRoutes("api/v1/user/verify-email")
    data object ResendVerifyEmail : VocaGoRoutes("api/v1/user/resend-verification-email")
    data object VerifyTwoFA : VocaGoRoutes("api/v1/user/verify-2fa")
    data object GetTwoFAQrCode : VocaGoRoutes("api/v1/user/get-2fa-qr-code")
    data object SetUpTwoFA : VocaGoRoutes("api/v1/user/setup-2fa")
    data object DisableTwoFA : VocaGoRoutes("api/v1/user/turn-off-2fa")
    data object LoginGoogle : VocaGoRoutes("api/v1/user/login-google")
    data object Logout : VocaGoRoutes("api/v1/user/logout")

    data object GetProfile : VocaGoRoutes("api/v1/user/me")
    data object ChangePassword : VocaGoRoutes("api/v1/user/change-password")

    data object GetNews : VocaGoRoutes("api/v1/news")
    data object GetNewsHistories : VocaGoRoutes("api/v1/news/history")
    data class AnswerNews(val id: String) : VocaGoRoutes("api/v1/news/$id/answer")
    data class ToggleBookmark(val id: String) : VocaGoRoutes("api/v1/news/$id/bookmark")

    data object GetDictionaryWord : VocaGoRoutes("/api/v1/dictionary/")
    data object GetSuggestWord : VocaGoRoutes("/api/v1/word/suggest")
    data object GetWordOfTheDay : VocaGoRoutes("/api/v1/dictionary/wordoftheday")

    data object GetToeicList : VocaGoRoutes("/api/v1/admin/toeic/tests")
    data class GetToeicDetail(val id: String) : VocaGoRoutes("/api/v1/admin/toeic/test/$id")
    data object SubmitTest : VocaGoRoutes("/api/v1/admin/toeic/test/submit")
    data class GetToeicResult(val id: String) : VocaGoRoutes("/api/v1/admin/toeic/test/result/$id")
    data class GetToeicResultDetail(val id: String) :
        VocaGoRoutes("/api/v1/admin/toeic/test/result_detail/$id")

    data object GetDevicesList : VocaGoRoutes("/api/v1/user/me/devices")
    data object LogoutUserDevice : VocaGoRoutes("/api/v1/user/me/devices")


    //novel
    data object GetNovel : VocaGoRoutes("/api/v1/fiction")
    data object GetReadNovel : VocaGoRoutes("/api/v1/fiction/read")

    //Chapter
    data class GetChapter(val id: String) : VocaGoRoutes("/api/v1/chapter/$id")

    //voca
    data object GetVoca : VocaGoRoutes("/api/v1/voca/me")
    data object VocaSync : VocaGoRoutes("/api/v1/voca/sync")

    //checkin
    data object SelectCheckIn : VocaGoRoutes("/api/v1/user/select-check-in")
    data object CheckIn : VocaGoRoutes("/api/v1/user/check-in")
    data object GetRanking : VocaGoRoutes("/api/v1/user/ranking-table")

    //premium
    data object CreatePayment : VocaGoRoutes("/api/v1/payment")
    data object MoMoPay : VocaGoRoutes("/api/v1/momo/ipn")

}
