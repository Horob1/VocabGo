package com.acteam.vocago.domain.usecase

import com.acteam.vocago.data.local.entity.VocaListEntity
import com.acteam.vocago.domain.repository.VocaRepository

class DeleteVocaListUseCase(
    private val vocaListRepository: VocaRepository,
) {
    suspend operator fun invoke(vocaList: VocaListEntity) {
        vocaListRepository.deleteVocaList(vocaList)
    }
}