package com.acteam.vocago.domain.usecase

import com.acteam.vocago.domain.repository.TTSRepository

class GetTTSConfigUseCase(
    private val ttsRepository: TTSRepository,
) {
    operator fun invoke() = ttsRepository.getConfiguration()
}