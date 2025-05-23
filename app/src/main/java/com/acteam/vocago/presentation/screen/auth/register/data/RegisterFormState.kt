package com.acteam.vocago.presentation.screen.auth.register.data

data class RegisterFormState(
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val phone: String = "",
    val address: String = "",
    val dateOfBirth: String = "",
    val gender: String = "",
    val isPasswordVisible: Boolean = false,
    val isConfirmPasswordVisible: Boolean = false
)