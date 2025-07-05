package com.acteam.vocago.domain.usecase

import com.acteam.vocago.data.local.entity.VocaEntity
import com.acteam.vocago.domain.repository.VocaRepository

class SaveWordToVocaListUseCase(
    private val vocaRepository: VocaRepository,
) {
    suspend operator fun invoke(word: VocaEntity) {
        vocaRepository.insertVoca(word)
    }
}