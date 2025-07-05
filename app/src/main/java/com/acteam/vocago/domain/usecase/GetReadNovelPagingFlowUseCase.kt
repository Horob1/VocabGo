package com.acteam.vocago.domain.usecase

import androidx.paging.PagingData
import com.acteam.vocago.domain.model.Novel
import com.acteam.vocago.domain.repository.NovelRepository
import kotlinx.coroutines.flow.Flow

class GetReadNovelPagingFlowUseCase(
    private val novelRepository: NovelRepository,
) {
    operator fun invoke(): Flow<PagingData<Novel>> = novelRepository.getReadNovelPagingFlow()
}