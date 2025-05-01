package com.acteam.vocago.domain.usecase

import com.acteam.vocago.domain.model.AppLanguage
import com.acteam.vocago.domain.repository.LanguageRepository

class ChooseLanguageUserCase(private val languageRepository: LanguageRepository) {
    operator fun invoke(appLanguage: AppLanguage) {
        languageRepository.chooseLanguage(appLanguage)
    }
}