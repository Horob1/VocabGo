package com.acteam.vocago.domain.usecase

import com.acteam.vocago.domain.model.TTSConfig
import com.acteam.vocago.domain.repository.TTSRepository

class SaveTTSConfigUseCase(
    private val ttsRepository: TTSRepository,
) {
    suspend operator fun invoke(config: TTSConfig) = ttsRepository.saveConfiguration(config)
}