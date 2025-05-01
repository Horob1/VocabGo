package com.acteam.vocago.domain.usecase

import com.acteam.vocago.domain.repository.LanguageRepository

class GetLanguageUseCase(private val languageRepository: LanguageRepository) {
    operator fun invoke(): String {
        return languageRepository.getLanguage()
    }
}
