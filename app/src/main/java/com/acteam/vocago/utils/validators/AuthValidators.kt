package com.acteam.vocago.utils.validators

object AuthValidators {
    private val EMAIL_REGEX = Regex(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$"
    )

    fun isValidEmail(email: String): Boolean {
        return EMAIL_REGEX.matches(email)
    }
}