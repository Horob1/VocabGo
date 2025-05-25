package com.acteam.vocago.domain.usecase

import com.acteam.vocago.domain.local.NewsLocalDataSource
import kotlinx.coroutines.flow.Flow

class GetChosenNewsCategoriesUseCase(
    private val newsLocalDataSource: NewsLocalDataSource,
) {
    operator fun invoke(): Flow<List<String>> = newsLocalDataSource.getChosenCategories()
}