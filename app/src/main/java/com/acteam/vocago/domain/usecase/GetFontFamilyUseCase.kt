package com.acteam.vocago.domain.usecase

import com.acteam.vocago.domain.model.NovelFont
import com.acteam.vocago.domain.repository.ReadNovelSettingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetFontFamilyUseCase(
    private val readNovelSettingRepository: ReadNovelSettingRepository,
) {
    operator fun invoke(): Flow<NovelFont> {
        return readNovelSettingRepository.getFontName().map { fontName ->
            NovelFont.getFontByName(fontName)
        }
    }
}