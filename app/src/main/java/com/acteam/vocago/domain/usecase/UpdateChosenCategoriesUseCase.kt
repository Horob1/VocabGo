package com.acteam.vocago.domain.usecase

import com.acteam.vocago.domain.local.NewsLocalDataSource

class UpdateChosenCategoriesUseCase(
    private val newsRepository: NewsLocalDataSource,
) {
    suspend operator fun invoke(categories: List<String>) {
        newsRepository.updateChosenCategories(categories)
    }
}