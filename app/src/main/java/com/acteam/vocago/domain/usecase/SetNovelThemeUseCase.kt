package com.acteam.vocago.domain.usecase

import com.acteam.vocago.domain.repository.ReadNovelSettingRepository

class SetNovelThemeUseCase(
    private val readNovelSettingRepository: ReadNovelSettingRepository,
) {
    suspend operator fun invoke(themeName: String) {
        readNovelSettingRepository.setSelectedTheme(themeName)
    }
}