package com.acteam.vocago.domain.usecase

import androidx.paging.PagingData
import com.acteam.vocago.data.local.entity.NewsHistoryEntity
import com.acteam.vocago.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow

class GetNewsHistoryPagingUseCase(
    private val newsRepository: NewsRepository,
) {
    operator fun invoke(): Flow<PagingData<NewsHistoryEntity>> =
        newsRepository.getNewsHistoriesPagingFlow()
}