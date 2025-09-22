package com.acteam.vocago.domain.usecase

import com.acteam.vocago.domain.repository.NovelRepository

class GetReadNovelFirstPageUseCase(
    private val novelRepository: NovelRepository,
) {
    suspend operator fun invoke() = novelRepository.getReadNovelFirstPage()
}