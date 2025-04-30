package com.acteam.vocago.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class NavScreen() {
    @Serializable object WelcomeNavScreen : NavScreen()
    @Serializable object AuthNavScreen : NavScreen()
    @Serializable object LoginNavScreen : NavScreen()
    @Serializable object RegisterNavScreen : NavScreen()
    @Serializable object HomeNavScreen : NavScreen()
}