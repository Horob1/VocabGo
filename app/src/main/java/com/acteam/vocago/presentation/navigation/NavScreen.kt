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
    data class TwoFACodeNavScreen(val email: String) : NavScreen()

    //Mâm 2 của Main
    @Serializable
    data object NewsNavScreen : NavScreen()
    // Mâm 3
}