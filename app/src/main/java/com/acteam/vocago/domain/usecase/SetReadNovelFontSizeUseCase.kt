package com.acteam.vocago.domain.usecase

import com.acteam.vocago.domain.repository.ReadNovelSettingRepository

class SetReadNovelFontSizeUseCase(
    private val readNovelSettingRepository: ReadNovelSettingRepository,
) {
    suspend operator fun invoke(fontSize: Float) {
        readNovelSettingRepository.setFontSize(fontSize)
    }
}