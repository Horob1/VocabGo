package com.acteam.vocago.domain.usecase

import com.acteam.vocago.domain.model.NovelDetail
import com.acteam.vocago.domain.repository.NovelRepository

class GetNovelDetailUseCase(
    private val novelRepository: NovelRepository,
) {
    suspend operator fun invoke(id: String): NovelDetail? {
        return novelRepository.getNovelDetail(id).getOrNull()
    }

}