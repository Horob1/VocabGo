package com.acteam.vocago.domain.usecase

import com.acteam.vocago.domain.model.Novel
import com.acteam.vocago.domain.repository.NovelRepository
import kotlinx.coroutines.flow.Flow

class GetReadNovelFlowUseCase(
    private val novelRepository: NovelRepository,
) {
    operator fun invoke(): Flow<List<Novel>> = novelRepository.getReadNovelFlow()
}