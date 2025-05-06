package com.acteam.vocago.presentation.screen.auth.login.data

data class LoginFormState(
    val username: String = "",
    val password: String = "",
    val passwordVisible: Boolean = false,
    val isLoginButtonEnabled: Boolean = false
)