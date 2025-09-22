package com.acteam.vocago.domain.usecase

import com.acteam.vocago.domain.repository.NovelRepository

class GetChapterUseCase(
    private val novelRepository: NovelRepository,
) {
    suspend operator fun invoke(chapterId: String) =
        novelRepository.getChapterDetail(chapterId).getOrNull()
}