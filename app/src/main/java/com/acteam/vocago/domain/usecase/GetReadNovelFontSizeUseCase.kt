package com.acteam.vocago.domain.usecase

import com.acteam.vocago.domain.repository.ReadNovelSettingRepository
import kotlinx.coroutines.flow.Flow

class GetReadNovelFontSizeUseCase(
    private val readNovelSettingRepository: ReadNovelSettingRepository,
) {
    operator fun invoke(): Flow<Float> = readNovelSettingRepository.getFontSize()


}