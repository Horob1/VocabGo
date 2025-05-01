package com.acteam.vocago.domain.model

sealed class AppTheme(val name: String) {
    object LIGHT : AppTheme("Light")
    object DARK : AppTheme("Dark")
    object SYSTEM : AppTheme("System")
}