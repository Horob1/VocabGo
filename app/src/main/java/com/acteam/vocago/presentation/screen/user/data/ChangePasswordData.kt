package com.acteam.vocago.presentation.screen.user.data


data class ChangePasswordState(
    val oldPassword: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isOldPasswordVisible: Boolean = false,
    val isPasswordVisible: Boolean = false,
    val isConfirmPasswordVisible: Boolean = false,
    val isChangePasswordButtonEnabled: Boolean = false
)
