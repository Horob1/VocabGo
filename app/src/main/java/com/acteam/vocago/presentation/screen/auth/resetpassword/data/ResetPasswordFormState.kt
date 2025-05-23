package com.acteam.vocago.presentation.screen.auth.resetpassword.data

data class ResetPasswordFormState(
    val password: String = "",
    val confirmPassword: String = "",
    val isPasswordVisible: Boolean = false,
    val isConfirmPasswordVisible: Boolean = false,
    val isResetPasswordButtonEnabled: Boolean = false
)