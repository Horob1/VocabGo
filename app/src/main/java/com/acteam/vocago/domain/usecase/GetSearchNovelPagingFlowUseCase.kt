package com.acteam.vocago.domain.usecase

import com.acteam.vocago.domain.repository.NovelRepository

class GetSearchNovelPagingFlowUseCase(
    private val novelRepository: NovelRepository,
) {
    operator fun invoke(keySearch: String) = novelRepository.getNovelPagingFlow(keySearch)
}