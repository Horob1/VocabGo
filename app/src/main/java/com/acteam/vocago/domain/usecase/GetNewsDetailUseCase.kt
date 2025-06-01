package com.acteam.vocago.domain.usecase

import com.acteam.vocago.domain.repository.NewsRepository

class GetNewsDetailUseCase(
    private val newsRepository: NewsRepository,
) {
    suspend operator fun invoke(id: String) = newsRepository.getNewsDetail(id)
}