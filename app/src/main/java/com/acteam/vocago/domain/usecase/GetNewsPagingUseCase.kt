package com.acteam.vocago.domain.usecase

import androidx.paging.PagingData
import com.acteam.vocago.data.local.entity.NewsEntity
import com.acteam.vocago.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow

class GetNewsPagingUseCase(
    private val newsRepository: NewsRepository,
) {
    operator fun invoke(
        categories: List<String>,
        keySearch: String,
        level: String,
    ): Flow<PagingData<NewsEntity>> = newsRepository.getNewsPagingFlow(
        categories = categories,
        keySearch = keySearch,
        level = level
    )
}