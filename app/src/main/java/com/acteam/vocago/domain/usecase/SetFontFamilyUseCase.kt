package com.acteam.vocago.domain.usecase

import com.acteam.vocago.domain.repository.ReadNovelSettingRepository

class SetFontFamilyUseCase(
    private val readNovelSettingRepository: ReadNovelSettingRepository,
) {
    suspend operator fun invoke(fontName: String) {
        readNovelSettingRepository.setFontName(fontName)
    }
}