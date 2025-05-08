package com.acteam.vocago.presentation.screen.auth.resetpassword.data

data class ResetPasswordFormState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isPasswordVisible: Boolean = false,
    val isConfirmPasswordVisible: Boolean = false,
    val isResetPasswordButtonEnabled: Boolean = false
)