package com.acteam.vocago.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class NavScreen() {
    @Serializable
    data object WelcomeNavScreen : NavScreen()

    @Serializable
    data object AuthNavScreen : NavScreen()

    @Serializable
    data object LoginNavScreen : NavScreen()

    @Serializable
    data object RegisterNavScreen : NavScreen()

    @Serializable
    data object HomeNavScreen : NavScreen()

    @Serializable
    data object ForgotPasswordNavScreen : NavScreen()

    @Serializable
    data class ResetPasswordNavScreen(val email: String) : NavScreen()

    @Serializable
    data class VerifyEmailNavScreen(val email: String) : NavScreen()
}