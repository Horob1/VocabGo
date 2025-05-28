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
    data object LoginGoogle : VocaGoRoutes("api/v1/user/login-google")

    data object GetProfile : VocaGoRoutes("api/v1/user/me")

    data object GetNews : VocaGoRoutes("api/v1/news")
}
