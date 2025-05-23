package com.acteam.vocago.domain.model

sealed class AppLanguage(val languageCode: String) {
    data object English : AppLanguage("en")
    data object Vietnamese : AppLanguage("vi")
    data object System : AppLanguage("sys")
}