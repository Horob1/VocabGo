package com.acteam.vocago.domain.usecase

import com.acteam.vocago.data.local.entity.VocaListEntity
import com.acteam.vocago.domain.repository.VocaRepository

class CreateVocaListUseCase(
    private val vocaRepository: VocaRepository,
) {
    suspend operator fun invoke(vocaList: VocaListEntity) {
        vocaRepository.insertVocaList(vocaList)
    }
}