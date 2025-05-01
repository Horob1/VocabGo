package com.acteam.vocago.domain.model

sealed class AppLanguage(val languageCode: String) {
    object English : AppLanguage("en")
    object Vietnamese : AppLanguage("vi")
    object System : AppLanguage("sys")
}