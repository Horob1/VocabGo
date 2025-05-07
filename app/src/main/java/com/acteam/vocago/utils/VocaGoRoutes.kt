package com.acteam.vocago.utils

sealed class VocaGoRoutes(val path: String) {
    object Login : VocaGoRoutes("api/v1/user/login")
    object Register : VocaGoRoutes("api/v1/user/register")
    object ForgotPassword : VocaGoRoutes("api/v1/user/forgot-password")
}
