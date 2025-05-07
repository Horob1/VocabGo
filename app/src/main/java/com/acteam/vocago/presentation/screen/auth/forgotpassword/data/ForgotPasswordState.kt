package com.acteam.vocago.presentation.screen.auth.forgotpassword.data

data class ForgotPasswordState(
    val email: String = "",
    val isForgotPasswordButtonEnabled: Boolean = false
)