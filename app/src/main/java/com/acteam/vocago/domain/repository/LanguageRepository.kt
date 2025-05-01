package com.acteam.vocago.domain.repository

import com.acteam.vocago.domain.model.AppLanguage

interface LanguageRepository {
    fun chooseLanguage(appLanguage: AppLanguage)
    fun getLanguage(): String
}