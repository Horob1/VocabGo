package com.acteam.vocago.utils

sealed class VocaGoRoutes(val path: String) {
    object Login : VocaGoRoutes("api/v1/user/login")
    object Register : VocaGoRoutes("api/v1/user/register")
    object ForgotPassword : VocaGoRoutes("api/v1/user/forgot-password")
    object ResetPassword : VocaGoRoutes("api/v1/user/reset-password")
    object RefreshToken : VocaGoRoutes("api/v1/user/refresh-token")
    object VerifyEmail : VocaGoRoutes("api/v1/user/verify-email")
    object ResendVerifyEmail : VocaGoRoutes("/api/v1/user/resend-verification-email")
    object VerifyTwoFA : VocaGoRoutes("api/v1/user/verify-2fa")
    object LoginGoogle : VocaGoRoutes("api/v1/user/login-google")
}
