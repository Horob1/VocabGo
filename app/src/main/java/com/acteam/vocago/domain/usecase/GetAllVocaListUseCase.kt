package com.acteam.vocago.domain.usecase

import com.acteam.vocago.domain.repository.VocaRepository

class GetAllVocaListUseCase(
    val repository: VocaRepository,
) {
    operator fun invoke() = repository.getAllVocaLists()
}