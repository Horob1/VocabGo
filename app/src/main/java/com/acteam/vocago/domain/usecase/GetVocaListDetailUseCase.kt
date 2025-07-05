package com.acteam.vocago.domain.usecase

import com.acteam.vocago.data.local.entity.VocaListWithVocas
import com.acteam.vocago.domain.repository.VocaRepository
import kotlinx.coroutines.flow.Flow

class GetVocaListDetailUseCase(
    private val repository: VocaRepository,
) {
    operator fun invoke(listId: Int): Flow<VocaListWithVocas> {
        return repository.getVocaListWithVocas(listId)
    }

}