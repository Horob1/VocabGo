package com.acteam.vocago.domain.usecase

import com.acteam.vocago.domain.model.NovelTheme
import com.acteam.vocago.domain.repository.ReadNovelSettingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetReadNovelThemeUseCase(
    private val readNovelSettingRepository: ReadNovelSettingRepository,
) {
    operator fun invoke(): Flow<NovelTheme> {
        return readNovelSettingRepository.getSelectedTheme().map { themeName ->
            NovelTheme.getThemeByName(themeName)
        }
    }
}